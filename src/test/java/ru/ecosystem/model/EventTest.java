package ru.ecosystem.model;

import org.junit.jupiter.api.Test;
import ru.ecosystem.model.event.*;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    @Test
    void testBirthEvent() {
        Position parent = new Position(1, 1);
        Position child = new Position(1, 2);
        BirthEvent event = new BirthEvent(5, "H", parent, child);
        
        assertEquals(5, event.tick());
        assertEquals("H", event.type());
        assertEquals(parent, event.parent());
        assertEquals(child, event.child());
    }

    @Test
    void testDeathEvent() {
        Position pos = new Position(3, 4);
        DeathEvent event = new DeathEvent(10, "P", pos, DeathCause.STARVATION);
        
        assertEquals(10, event.tick());
        assertEquals("P", event.type());
        assertEquals(pos, event.where());
        assertEquals(DeathCause.STARVATION, event.cause());
    }

    @Test
    void testEatEvent() {
        Position pos = new Position(0, 0);
        EatEvent event = new EatEvent(2, pos, "P", "H");
        
        assertEquals(2, event.tick());
        assertEquals(pos, event.where());
        assertEquals("P", event.eaterType());
        assertEquals("H", event.foodType());
    }

    @Test
    void testMoveEvent() {
        Position from = new Position(0, 0);
        Position to = new Position(1, 0);
        MoveEvent event = new MoveEvent(1, "H", from, to);
        
        assertEquals(1, event.tick());
        assertEquals("H", event.type());
        assertEquals(from, event.from());
        assertEquals(to, event.to());
    }

    @Test
    void testGrassGrowthEvent() {
        Position pos = new Position(2, 2);
        GrassGrowthEvent event = new GrassGrowthEvent(3, pos, 2);
        
        assertEquals(3, event.tick());
        assertEquals(pos, event.where());
        assertEquals(2, event.amount());
    }
}