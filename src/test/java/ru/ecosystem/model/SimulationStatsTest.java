package ru.ecosystem.model;

import org.junit.jupiter.api.Test;
import ru.ecosystem.model.stats.GenerationStats;

import static org.junit.jupiter.api.Assertions.*;

class SimulationStatsTest {

    @Test
    void testInitialCountsMatchConfig() {
        SimulationConfig config = SimulationConfig.builder()
                .width(10).height(10)
                .initialGrassCells(20)
                .initialHerbivores(5)
                .initialPredators(2)
                .build();

        Simulation sim = new Simulation(config);
        GenerationStats stats = sim.stats();

        assertEquals(5, stats.herbivores());
        assertEquals(2, stats.predators());
        assertEquals(20 * config.grassMaxLevel(), stats.grassTotalLevel());
    }

    @Test
    void testSnapshotSize() {
        SimulationConfig config = SimulationConfig.builder()
                .width(5).height(4)
                .initialGrassCells(10)  
                .initialHerbivores(2)
                .initialPredators(1)
                .build();
        Simulation sim = new Simulation(config);
        
        SimulationSnapshot snapshot = sim.snapshot();
        assertEquals(5 * 4, snapshot.cells().size());
        assertEquals(0, snapshot.tick());
    }
}