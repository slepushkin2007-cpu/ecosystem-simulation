package ru.ecosystem.model;

import org.junit.jupiter.api.Test;
import ru.ecosystem.model.exception.OutOfGridException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    @Test
    void testGridCreation() {
        Grid grid = new Grid(10, 10);
        assertEquals(10, grid.width());
        assertEquals(10, grid.height());
    }

    @Test
    void testNegativeSizeThrowsException() {
        assertThrows(OutOfGridException.class, () -> new Grid(-1, 10));
        assertThrows(OutOfGridException.class, () -> new Grid(10, 0));
    }

    @Test
    void testCellAccessWithinBounds() {
        Grid grid = new Grid(5, 5);
        Cell cell = grid.cell(new Position(4, 4));
        assertNotNull(cell);
    }

    @Test
    void testCellAccessOutsideBoundsThrowsException() {
        Grid grid = new Grid(5, 5);
        assertThrows(OutOfGridException.class, () -> grid.cell(new Position(5, 0)));
        assertThrows(OutOfGridException.class, () -> grid.cell(new Position(0, -1)));
    }

    @Test
    void testNeighborsCenter() {
        Grid grid = new Grid(5, 5);
        List<Position> neighbors = grid.neighbors4(new Position(2, 2));
        assertEquals(4, neighbors.size());
        assertTrue(neighbors.contains(new Position(1, 2)));
        assertTrue(neighbors.contains(new Position(3, 2)));
        assertTrue(neighbors.contains(new Position(2, 1)));
        assertTrue(neighbors.contains(new Position(2, 3)));
    }

    @Test
    void testNeighborsCorner() {
        Grid grid = new Grid(5, 5);
        List<Position> neighbors = grid.neighbors4(new Position(0, 0));
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(new Position(1, 0)));
        assertTrue(neighbors.contains(new Position(0, 1)));
    }

    @Test
    void testNeighborsGrid1x1() {
        Grid grid = new Grid(1, 1);
        List<Position> neighbors = grid.neighbors4(new Position(0, 0));
        assertTrue(neighbors.isEmpty());
    }
}