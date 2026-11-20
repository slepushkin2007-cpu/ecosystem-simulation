package ru.ecosystem.model.entity;

import ru.ecosystem.model.Position;

public class Predator extends Animal {
    public Predator(Position position, int energy, int age) {
        super(position, energy, age);
    }

    public Predator child(Position position) {
        return new Predator(position, 1, 0);
    }
}