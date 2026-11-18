package ru.ecosystem.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulationOrderIndependenceTest {

    @Test
    void testOrderIndependence() {
        // Создаем две симуляции с ОДИНАКОВЫМ сидом
        SimulationConfig config = SimulationConfig.builder()
                .width(10).height(10)
                .seed(777L)
                .initialGrassCells(20)
                .initialHerbivores(10)
                .initialPredators(5)
                .build();

        Simulation sim1 = new Simulation(config);
        Simulation sim2 = new Simulation(config);

        for (int i = 0; i < 10; i++) {
            sim1.step();
            sim2.step();
        }

        // Снимки должны быть абсолютно идентичны
        SimulationSnapshot snap1 = sim1.snapshot();
        SimulationSnapshot snap2 = sim2.snapshot();

        assertEquals(snap1.cells().size(), snap2.cells().size());
        for (int i = 0; i < snap1.cells().size(); i++) {
            assertEquals(snap1.cells().get(i), snap2.cells().get(i), "Клетки должны совпадать");
        }
    }
}