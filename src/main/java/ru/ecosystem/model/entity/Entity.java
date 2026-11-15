package ru.ecosystem.model.entity;

import ru.ecosystem.model.Position;

/**
 * Базовая сущность, находящаяся на решётке.
 */
public abstract class Entity {
    private Position position;

    protected Entity(Position position) {
        this.position = position;
    }

    public Position position() { return position; }

    public void moveTo(Position newPosition) {
        this.position = newPosition;
    }
}