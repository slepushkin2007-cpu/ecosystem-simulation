package ru.ecosystem.model.entity;

import ru.ecosystem.model.Position;

/**
 * Растительность на клетке. Имеет текущий уровень и максимум.
 */
public class Grass extends Entity {
    private int level;

    public Grass(Position position, int level) {
        super(position);
        this.level = level;
    }

    public int level() { return level; }

    /**
     * Прирост травы с ограничением максимума.
     * @return фактический прирост
     */
    public int grow(int amount, int maxLevel) {
        int before = level;
        level = Math.min(maxLevel, level + amount);
        return level - before;
    }

    /**
     * Съесть траву, не больше доступного.
     * @return сколько реально съедено
     */
    public int consume(int amount) {
        int eaten = Math.min(level, amount);
        level -= eaten;
        return eaten;
    }

    public boolean isEmpty() { return level <= 0; }
}