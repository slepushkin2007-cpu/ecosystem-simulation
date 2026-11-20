package ru.ecosystem.model.entity;

import ru.ecosystem.model.Position;

public abstract class Animal extends Entity {
    private int energy;
    private int age;

    protected Animal(Position position, int energy, int age) {
        super(position);
        this.energy = energy;
        this.age = age;
    }

    public int energy() { return energy; }
    public int age() { return age; }

    public void addEnergy(int delta) {
        this.energy = Math.max(0, this.energy + delta);
    }

    public void consumeEnergy(int delta) {
        this.energy = Math.max(0, this.energy - delta);
    }

    public void incrementAge() { this.age++; }

    public boolean isAlive(int maxAge) {
        return energy > 0 && age <= maxAge;
    }
}