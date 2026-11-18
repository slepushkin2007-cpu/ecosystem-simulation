package ru.ecosystem.app;

import ru.ecosystem.model.GenerationResult;
import ru.ecosystem.model.Simulation;
import ru.ecosystem.model.SimulationConfig;
import ru.ecosystem.output.ConsolePrinter;

/**
 * Точка входа консольного приложения.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        // Дефолтные настройки
        int width = 20;
        int height = 20;
        long seed = 42L;
        int ticks = 10;
        int herbivores = 20;
        int predators = 5;
        int grass = 80;

        // Простой разбор аргументов
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--width" -> width = Integer.parseInt(args[++i]);
                case "--height" -> height = Integer.parseInt(args[++i]);
                case "--seed" -> seed = Long.parseLong(args[++i]);
                case "--ticks" -> ticks = Integer.parseInt(args[++i]);
                case "--herbivores" -> herbivores = Integer.parseInt(args[++i]);
                case "--predators" -> predators = Integer.parseInt(args[++i]);
                case "--grass" -> grass = Integer.parseInt(args[++i]);
                default -> {
                    System.err.println("Неизвестный аргумент: " + args[i]);
                    System.exit(1);
                }
            }
        }

        SimulationConfig config = SimulationConfig.builder()
                .width(width)
                .height(height)
                .seed(seed)
                .initialHerbivores(herbivores)
                .initialPredators(predators)
                .initialGrassCells(grass)
                .build();

        Simulation sim = new Simulation(config);
        ConsolePrinter printer = new ConsolePrinter();

        System.out.println("=== Старт симуляции ===");
        System.out.printf("Поле: %dx%d, Сид: %d, Поколений: %d%n", width, height, seed, ticks);
        
        for (int i = 0; i < ticks; i++) {
            GenerationResult result = sim.step();
            
            // Печатаем все события, которые произошли за этот шаг
            result.events().forEach(printer::onEvent);

            // Каждые 5 шагов (или при завершении) печатаем статистику
            if (result.tick() % 5 == 0 || result.finished()) {
                System.out.printf("--- Поколение %d: Трава=%d, Травоядные=%d, Хищники=%d ---%n",
                        result.tick(),
                        result.stats().grassTotalLevel(),
                        result.stats().herbivores(),
                        result.stats().predators());
            }

            if (result.finished()) {
                System.out.println("Симуляция завершена: " + result.finishReason());
                break;
            }
        }
        System.out.println("=== Конец симуляции ===");
    }
}