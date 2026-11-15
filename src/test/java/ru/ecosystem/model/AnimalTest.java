package ru.ecosystem.model;

import org.junit.jupiter.api.Test;
import ru.ecosystem.model.entity.Herbivore;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    @Test
    void testEnergyChanges() {
        Herbivore h = new Herbivore(new Position(1, 1), 10, 0);
        h.consumeEnergy(3);
        assertEquals(7, h.energy());

        h.addEnergy(5);
        assertEquals(12, h.energy());
    }

    @Test
    void testEnergyDoesNotGoBelowZero() {
        Herbivore h = new Herbivore(new Position(1, 1), 2, 0);
        h.consumeEnergy(5);
        assertEquals(0, h.energy());
    }

    @Test
    void testAgeIncrementAndAliveStatus() {
        Herbivore h = new Herbivore(new Position(1, 1), 5, 29);
        assertTrue(h.isAlive(30));
        
        h.incrementAge(); // возраст 30
        assertTrue(h.isAlive(30));
        
        h.incrementAge(); // возраст 31
        assertFalse(h.isAlive(30)); // умер от старости
    }

    @Test
    void testDeathFromStarvation() {
        Herbivore h = new Herbivore(new Position(1, 1), 1, 0);
        h.consumeEnergy(2);
        assertFalse(h.isAlive(100)); // энергия 0, умер от голода
    }
}