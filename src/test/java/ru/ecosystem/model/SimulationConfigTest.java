package ru.ecosystem.model;

import org.junit.jupiter.api.Test;
import ru.ecosystem.model.exception.InvalidConfigException;

import static org.junit.jupiter.api.Assertions.*;

class SimulationConfigTest {

    @Test
    void testDefaultConfig() {
        SimulationConfig config = SimulationConfig.builder().build();
        assertEquals(20, config.width());
        assertEquals(20, config.height());
        assertEquals(42L, config.seed());
    }

    @Test
    void testCustomConfig() {
        SimulationConfig config = SimulationConfig.builder()
                .width(10)
                .height(15)
                .seed(100L)
                .initialHerbivores(5)
                .build();
        
        assertEquals(10, config.width());
        assertEquals(15, config.height());
        assertEquals(100L, config.seed());
        assertEquals(5, config.initialHerbivores());
    }

    @Test
    void testInvalidWidthThrowsException() {
        assertThrows(InvalidConfigException.class, () -> 
            SimulationConfig.builder().width(0).build()
        );
    }

    @Test
    void testNegativeHeightThrowsException() {
        assertThrows(InvalidConfigException.class, () -> 
            SimulationConfig.builder().height(-5).build()
        );
    }

    @Test
    void testNegativeMetabolismThrowsException() {
        assertThrows(InvalidConfigException.class, () -> 
            SimulationConfig.builder().herbivoreMetabolism(-1).build()
        );
    }

    @Test
    void testTooManyInitialGrassThrowsException() {
        assertThrows(InvalidConfigException.class, () -> 
            SimulationConfig.builder()
                .width(5)
                .height(5) // всего 25 клеток
                .initialGrassCells(30) // больше, чем клеток
                .build()
        );
    }
}