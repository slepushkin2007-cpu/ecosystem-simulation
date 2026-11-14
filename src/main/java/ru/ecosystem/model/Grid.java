package ru.ecosystem.model;

import ru.ecosystem.model.exception.OutOfGridException;

import java.util.ArrayList;
import java.util.List;

/**
 * Двумерная решётка клеток.
 */
public class Grid {
    private final int width;
    private final int height;
    private final Cell[][] cells;

    public Grid(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new OutOfGridException("Размер решётки должен быть положительным: " + width + "x" + height);
        }
        this.width = width;
        this.height = height;
        this.cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell();
            }
        }
    }

    public int width() { return width; }
    public int height() { return height; }

    public boolean inside(Position p) {
        return p.x() >= 0 && p.x() < width && p.y() >= 0 && p.y() < height;
    }

    public Cell cell(Position p) {
        if (!inside(p)) {
            throw new OutOfGridException("Позиция вне решётки: " + p);
        }
        return cells[p.x()][p.y()];
    }

    /**
     * Соседи по 4 направлениям, только внутри решётки.
     */
    public List<Position> neighbors4(Position p) {
        List<Position> result = new ArrayList<>(4);
        int[][] deltas = {{1,0},{-1,0},{0,1},{0,-1}};
        for (int[] d : deltas) {
            Position n = new Position(p.x() + d[0], p.y() + d[1]);
            if (inside(n)) result.add(n);
        }
        return result;
    }
}