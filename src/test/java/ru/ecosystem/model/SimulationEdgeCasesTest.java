package ru.ecosystem.model;

import org.junit.jupiter.api.Test;
import ru.ecosystem.model.exception.OutOfGridException;

import static org.junit.jupiter.api.Assertions.*;

class SimulationEdgeCasesTest {

    @Test
    void testZeroSizeGridThrowsException() {
        assertThrows(OutOfGridException.class, () -> new Grid(0, 10));
        assertThrows(OutOfGridException.class, () -> new Grid(10, 0));
    }

        @Test
    void testOutOfGridNeighborsOnEdge() {
        Grid grid = new Grid(3, 3);
        assertEquals(2, grid.neighbors4(new Position(0, 0)).size());
        assertEquals(3, grid.neighbors4(new Position(1, 0)).size());
        assertEquals(4, grid.neighbors4(new Position(1, 1)).size());
    }

    @Test
    void testEmptyGridStats() {
        SimulationConfig config = SimulationConfig.builder()
                .width(5).height(5)
                .initialGrassCells(0)
                .initialHerbivores(0)
                .initialPredators(0)
                .build();
        Simulation sim = new Simulation(config);
        
        assertEquals(0, sim.stats().grassTotalLevel());
        assertEquals(0, sim.stats().herbivores());
        assertEquals(0, sim.stats().predators());
    }
}