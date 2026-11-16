package ru.ecosystem.model;

import ru.ecosystem.model.entity.*;
import ru.ecosystem.model.stats.GenerationStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Движок симуляции экосистемы.
 * Класс не пишет в консоль и не знает о слое вывода.
 */
public class Simulation {
    private final SimulationConfig config;
    private final Random random;
    private final Grid grid;
    private int tick = 0;

    public Simulation(SimulationConfig config) {
        this.config = config;
        this.random = new Random(config.seed());
        this.grid = new Grid(config.width(), config.height());
        placeInitial();
    }

    public int tick() { return tick; }
    public Grid grid() { return grid; }
    public SimulationConfig config() { return config; }

    private void placeInitial() {
        // трава
        int placedGrass = 0;
        int guard = 0;
        while (placedGrass < config.initialGrassCells() && guard++ < 100_000) {
            Position p = randomPosition();
            if (!grid.cell(p).hasGrass()) {
                grid.cell(p).setGrass(new Grass(p, config.grassMaxLevel()));
                placedGrass++;
            }
        }
        // травоядные
        int placedH = 0;
        guard = 0;
        while (placedH < config.initialHerbivores() && guard++ < 100_000) {
            Position p = randomPosition();
            if (grid.cell(p).isEmpty()) {
                grid.cell(p).setAnimal(new Herbivore(p, config.herbivoreStartEnergy(), 0));
                placedH++;
            }
        }
        // хищники
        int placedP = 0;
        guard = 0;
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

    /**
     * Снимок текущего состояния (для тестов и для слоя вывода).
     */
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

    /**
     * Статистика текущего состояния.
     */
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
}