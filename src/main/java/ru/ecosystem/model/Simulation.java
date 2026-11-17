package ru.ecosystem.model;

import ru.ecosystem.model.entity.*;
import ru.ecosystem.model.event.*;
import ru.ecosystem.model.stats.GenerationStats;

import java.util.*;

/**
 * Движок симуляции экосистемы.
 * Класс не пишет в консоль и не знает о слое вывода.
 */
public class Simulation {
    private final SimulationConfig config;
    private final Random random;
    private final Grid grid;
    private int tick = 0;
    private boolean finished = false;
    private String finishReason = null;

    public Simulation(SimulationConfig config) {
        this.config = config;
        this.random = new Random(config.seed());
        this.grid = new Grid(config.width(), config.height());
        placeInitial();
    }

    public int tick() { return tick; }
    public boolean finished() { return finished; }
    public String finishReason() { return finishReason; }
    public Grid grid() { return grid; }
    public SimulationConfig config() { return config; }

    private void placeInitial() {
        int placedGrass = 0, guard = 0;
        while (placedGrass < config.initialGrassCells() && guard++ < 100_000) {
            Position p = randomPosition();
            if (!grid.cell(p).hasGrass()) {
                grid.cell(p).setGrass(new Grass(p, config.grassMaxLevel()));
                placedGrass++;
            }
        }
        int placedH = 0; guard = 0;
        while (placedH < config.initialHerbivores() && guard++ < 100_000) {
            Position p = randomPosition();
            if (grid.cell(p).isEmpty()) {
                grid.cell(p).setAnimal(new Herbivore(p, config.herbivoreStartEnergy(), 0));
                placedH++;
            }
        }
        int placedP = 0; guard = 0;
        while (placedP < config.initialPredators() && guard++ < 100_000) {
            Position p = randomPosition();
            if (grid.cell(p).isEmpty()) {
                grid.cell(p).setAnimal(new Predator(p, config.predatorStartEnergy(), 0));
                placedP++;
            }
        }
    }

    private Position randomPosition() {
        return new Position(random.nextInt(config.width()), random.nextInt(config.height()));
    }

    public SimulationSnapshot snapshot() {
        List<SimulationSnapshot.CellState> states = new ArrayList<>();
        for (int x = 0; x < grid.width(); x++) {
            for (int y = 0; y < grid.height(); y++) {
                Position p = new Position(x, y);
                Cell c = grid.cell(p);
                int grass = c.grass() != null ? c.grass().level() : 0;
                String type = null;
                int energy = 0, age = 0;
                if (c.animal() instanceof Herbivore h) {
                    type = "H"; energy = h.energy(); age = h.age();
                } else if (c.animal() instanceof Predator pr) {
                    type = "P"; energy = pr.energy(); age = pr.age();
                }
                states.add(new SimulationSnapshot.CellState(p, grass, type, energy, age));
            }
        }
        return new SimulationSnapshot(tick, states);
    }

    public GenerationStats stats() {
        int grass = 0, herb = 0, pred = 0;
        for (int x = 0; x < grid.width(); x++) {
            for (int y = 0; y < grid.height(); y++) {
                Cell c = grid.cell(new Position(x, y));
                if (c.grass() != null) grass += c.grass().level();
                if (c.animal() instanceof Herbivore) herb++;
                else if (c.animal() instanceof Predator) pred++;
            }
        }
        return new GenerationStats(tick, grass, herb, pred, 0, 0);
    }

    // ==========================================
    // ОСНОВНАЯ ЛОГИКА ШАГА (ОДНОМОМЕНТНОСТЬ)
    // ==========================================

    /**
     * Один шаг поколения. Возвращает события и статистику.
     */
    public GenerationResult step() {
        if (finished) {
            return new GenerationResult(tick, List.of(), stats(), true, finishReason);
        }
        tick++;
        List<Event> events = new ArrayList<>();

        // 1) Рост травы
        for (int x = 0; x < grid.width(); x++) {
            for (int y = 0; y < grid.height(); y++) {
                Cell c = grid.cell(new Position(x, y));
                if (c.grass() != null) {
                    int grown = c.grass().grow(config.grassGrowthPerTick(), config.grassMaxLevel());
                    if (grown > 0) {
                        events.add(new GrassGrowthEvent(tick, c.grass().position(), grown));
                    }
                }
            }
        }

        // 2) Питание и движение
        List<Animal> allAnimals = collectAnimals();
        // Сортируем по стабильному ключу для детерминизма (позиция, тип)
        allAnimals.sort(Comparator
                .comparing((Animal a) -> a.position().x())
                .thenComparing(a -> a.position().y())
                .thenComparing(a -> a instanceof Herbivore ? "H" : "P"));

        List<Animal> survivors = new ArrayList<>();
        for (Animal a : allAnimals) {
            if (!isAliveOnGrid(a)) continue; // уже съеден в этом поколении
            if (a instanceof Herbivore h) {
                eatHerbivore(h, events);
                tryMove(a, events);
            } else if (a instanceof Predator p) {
                eatPredator(p, events);
                tryMove(a, events);
            }
            survivors.add(a);
        }

        // 3) Размножение
        List<Animal> newborns = new ArrayList<>();
        for (Animal a : survivors) {
            if (a instanceof Herbivore h && h.energy() >= config.herbivoreBreedEnergy()) {
                Herbivore child = h.child(a.position());
                h.addEnergy(-config.herbivoreMetabolism()); // цена размножения
                newborns.add(child);
                events.add(new BirthEvent(tick, "H", a.position(), a.position()));
            } else if (a instanceof Predator p && p.energy() >= config.predatorBreedEnergy()) {
                Predator child = p.child(a.position());
                p.addEnergy(-config.predatorMetabolism());
                newborns.add(child);
                events.add(new BirthEvent(tick, "P", a.position(), a.position()));
            }
        }
        // пытаемся разместить новорождённых на свободные клетки рядом с родителем или на его клетку
        for (Animal child : newborns) {
            Position spot = findFreeOrParentSpot(child.position());
            if (spot != null) {
                child.moveTo(spot);
                grid.cell(spot).setAnimal(child);
            }
        }

        // 4) Старение и смерть
        for (Animal a : survivors) {
            a.incrementAge();
            if (a instanceof Herbivore h) {
                h.consumeEnergy(config.herbivoreMetabolism());
                if (!h.isAlive(config.herbivoreMaxAge())) {
                    removeAnimal(h, events, h.energy() <= 0 ? DeathCause.STARVATION : DeathCause.OLD_AGE);
                }
            } else if (a instanceof Predator p) {
                p.consumeEnergy(config.predatorMetabolism());
                if (!p.isAlive(config.predatorMaxAge())) {
                    removeAnimal(p, events, p.energy() <= 0 ? DeathCause.STARVATION : DeathCause.OLD_AGE);
                }
            }
        }

        // 5) Проверка завершения
        GenerationStats stats = stats();
        if (stats.herbivores() == 0 && stats.predators() == 0) {
            finished = true;
            finishReason = "Полное вымирание";
        }

        return new GenerationResult(tick, events, stats, finished, finishReason);
    }

    // --- Вспомогательные методы ---

    private List<Animal> collectAnimals() {
        List<Animal> list = new ArrayList<>();
        for (int x = 0; x < grid.width(); x++)
            for (int y = 0; y < grid.height(); y++) {
                Animal a = grid.cell(new Position(x, y)).animal();
                if (a != null) list.add(a);
            }
        return list;
    }

    private boolean isAliveOnGrid(Animal a) {
        return grid.cell(a.position()).animal() == a;
    }

    private void eatHerbivore(Herbivore h, List<Event> events) {
        Cell here = grid.cell(h.position());
        if (here.hasGrass()) {
            int eaten = here.grass().consume(Math.min(3, config.grassMaxLevel()));
            if (eaten > 0) {
                h.addEnergy(eaten);
                events.add(new EatEvent(tick, h.position(), "H", "Grass"));
            }
        }
    }

    private void eatPredator(Predator p, List<Event> events) {
        for (Position n : grid.neighbors4(p.position())) {
            Animal prey = grid.cell(n).animal();
            if (prey instanceof Herbivore) {
                grid.cell(n).removeAnimal();
                p.addEnergy(config.predatorGainFromPrey());
                events.add(new EatEvent(tick, n, "P", "H"));
                events.add(new DeathEvent(tick, "H", n, DeathCause.EATEN));
                return;
            }
        }
    }

    private void tryMove(Animal a, List<Event> events) {
        if (random.nextDouble() < 0.5) return; // иногда не двигается
        List<Position> neighbors = new ArrayList<>(grid.neighbors4(a.position()));
        Collections.shuffle(neighbors, random);
        for (Position n : neighbors) {
            if (grid.cell(n).isEmpty()) {
                Position old = a.position();
                grid.cell(old).removeAnimal();
                a.moveTo(n);
                grid.cell(n).setAnimal(a);
                events.add(new MoveEvent(tick, a instanceof Herbivore ? "H" : "P", old, n));
                return;
            }
        }
    }

    private Position findFreeOrParentSpot(Position parent) {
        if (grid.cell(parent).isEmpty()) return parent;
        for (Position n : grid.neighbors4(parent)) {
            if (grid.cell(n).isEmpty()) return n;
        }
        return null;
    }

    private void removeAnimal(Animal a, List<Event> events, DeathCause cause) {
        Position p = a.position();
        if (grid.cell(p).animal() == a) {
            grid.cell(p).removeAnimal();
            events.add(new DeathEvent(tick, a instanceof Herbivore ? "H" : "P", p, cause));
        }
    }
}