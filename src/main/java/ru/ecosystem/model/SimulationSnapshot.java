package ru.ecosystem.model;

import java.util.List;

public final class SimulationSnapshot {
    public record CellState(Position pos, int grassLevel, String animalType, int energy, int age) {}

    private final int tick;
    private final List<CellState> cells;

    public SimulationSnapshot(int tick, List<CellState> cells) {
        this.tick = tick;
        this.cells = List.copyOf(cells);
    }

    public int tick() { return tick; }
    public List<CellState> cells() { return cells; }
}