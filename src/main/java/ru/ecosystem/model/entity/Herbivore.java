package ru.ecosystem.model.entity;

import ru.ecosystem.model.Position;

/**
 * Травоядное.
 */
public class Herbivore extends Animal {
    public Herbivore(Position position, int energy, int age) {
        super(position, energy, age);
    }

    public Herbivore child(Position position) {
        return new Herbivore(position, 1, 0);
    }
}