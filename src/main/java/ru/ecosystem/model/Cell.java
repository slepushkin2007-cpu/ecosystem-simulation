package ru.ecosystem.model;

import ru.ecosystem.model.entity.Animal;
import ru.ecosystem.model.entity.Grass;

/**
 * Клетка решётки. Хранит ссылки на траву и животное.
 */
public class Cell {
    private Grass grass;
    private Animal animal;

    public Grass grass() { return grass; }
    public Animal animal() { return animal; }

    public boolean hasGrass() { return grass != null && !grass.isEmpty(); }
    public boolean hasAnimal() { return animal != null; }
    public boolean isEmpty() { return !hasAnimal(); }

    public void setGrass(Grass grass) { this.grass = grass; }
    public void setAnimal(Animal animal) { this.animal = animal; }
    
    public Animal removeAnimal() {
        Animal a = animal;
        animal = null;
        return a;
    }
}