package ru.ecosystem.model.entity;

import ru.ecosystem.model.Position;

public class Grass extends Entity {
    private int level;

    public Grass(Position position, int level) {
        super(position);
        this.level = level;
    }

    public int level() { return level; }

    public int grow(int amount, int maxLevel) {
        int before = level;
        level = Math.min(maxLevel, level + amount);
        return level - before;
    }

    public int consume(int amount) {
        int eaten = Math.min(level, amount);
        level -= eaten;
        return eaten;
    }

    public boolean isEmpty() { return level <= 0; }
}