package ru.ecosystem.model;

import org.junit.jupiter.api.Test;
import ru.ecosystem.model.entity.Grass;

import static org.junit.jupiter.api.Assertions.*;

class GrassTest {

    @Test
    void testGrow() {
        Grass grass = new Grass(new Position(0, 0), 2);
        int grown = grass.grow(2, 5);
        assertEquals(2, grown);
        assertEquals(4, grass.level());
    }

    @Test
    void testGrowCappedAtMax() {
        Grass grass = new Grass(new Position(0, 0), 4);
        int grown = grass.grow(3, 5);
        assertEquals(1, grown); // выросла только на 1, так как максимум 5
        assertEquals(5, grass.level());
    }

    @Test
    void testConsume() {
        Grass grass = new Grass(new Position(0, 0), 5);
        int eaten = grass.consume(3);
        assertEquals(3, eaten);
        assertEquals(2, grass.level());
    }

    @Test
    void testConsumeMoreThanAvailable() {
        Grass grass = new Grass(new Position(0, 0), 2);
        int eaten = grass.consume(5);
        assertEquals(2, eaten);
        assertEquals(0, grass.level());
        assertTrue(grass.isEmpty());
    }
}