package ru.ecosystem.model;

import ru.ecosystem.model.exception.InvalidConfigException;

/**
 * Параметры симуляции. Все числовые «магические числа» живут здесь.
 * Класс неизменяемый (immutable), настройки задаются через Builder.
 */
public final class SimulationConfig {
    private final int width;
    private final int height;
    private final long seed;

    private final int grassMaxLevel;
    private final int grassGrowthPerTick;

    private final int herbivoreStartEnergy;
    private final int herbivoreMetabolism;
    private final int herbivoreBreedEnergy;
    private final int herbivoreMaxAge;

    private final int predatorStartEnergy;
    private final int predatorMetabolism;
    private final int predatorBreedEnergy;
    private final int predatorMaxAge;
    private final int predatorGainFromPrey;

    private final int initialHerbivores;
    private final int initialPredators;
    private final int initialGrassCells;

    private SimulationConfig(Builder b) {
        if (b.width <= 0) throw new InvalidConfigException("width <= 0");
        if (b.height <= 0) throw new InvalidConfigException("height <= 0");
        if (b.grassMaxLevel <= 0) throw new InvalidConfigException("grassMaxLevel <= 0");
        if (b.grassGrowthPerTick < 0) throw new InvalidConfigException("grassGrowthPerTick < 0");
        if (b.herbivoreMetabolism < 0) throw new InvalidConfigException("herbivoreMetabolism < 0");
        if (b.predatorMetabolism < 0) throw new InvalidConfigException("predatorMetabolism < 0");
        if (b.initialHerbivores < 0 || b.initialPredators < 0 || b.initialGrassCells < 0)
            throw new InvalidConfigException("initial counts < 0");
        if (b.initialGrassCells > b.width * b.height)
            throw new InvalidConfigException("initialGrassCells > area");

        this.width = b.width;
        this.height = b.height;
        this.seed = b.seed;
        this.grassMaxLevel = b.grassMaxLevel;
        this.grassGrowthPerTick = b.grassGrowthPerTick;
        this.herbivoreStartEnergy = b.herbivoreStartEnergy;
        this.herbivoreMetabolism = b.herbivoreMetabolism;
        this.herbivoreBreedEnergy = b.herbivoreBreedEnergy;
        this.herbivoreMaxAge = b.herbivoreMaxAge;
        this.predatorStartEnergy = b.predatorStartEnergy;
        this.predatorMetabolism = b.predatorMetabolism;
        this.predatorBreedEnergy = b.predatorBreedEnergy;
        this.predatorMaxAge = b.predatorMaxAge;
        this.predatorGainFromPrey = b.predatorGainFromPrey;
        this.initialHerbivores = b.initialHerbivores;
        this.initialPredators = b.initialPredators;
        this.initialGrassCells = b.initialGrassCells;
    }

    public static Builder builder() { return new Builder(); }

    public int width() { return width; }
    public int height() { return height; }
    public long seed() { return seed; }
    public int grassMaxLevel() { return grassMaxLevel; }
    public int grassGrowthPerTick() { return grassGrowthPerTick; }
    public int herbivoreStartEnergy() { return herbivoreStartEnergy; }
    public int herbivoreMetabolism() { return herbivoreMetabolism; }
    public int herbivoreBreedEnergy() { return herbivoreBreedEnergy; }
    public int herbivoreMaxAge() { return herbivoreMaxAge; }
    public int predatorStartEnergy() { return predatorStartEnergy; }
    public int predatorMetabolism() { return predatorMetabolism; }
    public int predatorBreedEnergy() { return predatorBreedEnergy; }
    public int predatorMaxAge() { return predatorMaxAge; }
    public int predatorGainFromPrey() { return predatorGainFromPrey; }
    public int initialHerbivores() { return initialHerbivores; }
    public int initialPredators() { return initialPredators; }
    public int initialGrassCells() { return initialGrassCells; }

    /**
     * Строитель для конфигурации.
     */
    public static final class Builder {
        private int width = 20, height = 20;
        private long seed = 42L;
        private int grassMaxLevel = 5;
        private int grassGrowthPerTick = 1;
        private int herbivoreStartEnergy = 5;
        private int herbivoreMetabolism = 1;
        private int herbivoreBreedEnergy = 8;
        private int herbivoreMaxAge = 30;
        private int predatorStartEnergy = 8;
        private int predatorMetabolism = 1;
        private int predatorBreedEnergy = 15;
        private int predatorMaxAge = 40;
        private int predatorGainFromPrey = 5;
        private int initialHerbivores = 20;
        private int initialPredators = 5;
        private int initialGrassCells = 80;

        public Builder width(int v) { width = v; return this; }
        public Builder height(int v) { height = v; return this; }
        public Builder seed(long v) { seed = v; return this; }
        public Builder grassMaxLevel(int v) { grassMaxLevel = v; return this; }
        public Builder grassGrowthPerTick(int v) { grassGrowthPerTick = v; return this; }
        public Builder herbivoreStartEnergy(int v) { herbivoreStartEnergy = v; return this; }
        public Builder herbivoreMetabolism(int v) { herbivoreMetabolism = v; return this; }
        public Builder herbivoreBreedEnergy(int v) { herbivoreBreedEnergy = v; return this; }
        public Builder herbivoreMaxAge(int v) { herbivoreMaxAge = v; return this; }
        public Builder predatorStartEnergy(int v) { predatorStartEnergy = v; return this; }
        public Builder predatorMetabolism(int v) { predatorMetabolism = v; return this; }
        public Builder predatorBreedEnergy(int v) { predatorBreedEnergy = v; return this; }
        public Builder predatorMaxAge(int v) { predatorMaxAge = v; return this; }
        public Builder predatorGainFromPrey(int v) { predatorGainFromPrey = v; return this; }
        public Builder initialHerbivores(int v) { initialHerbivores = v; return this; }
        public Builder initialPredators(int v) { initialPredators = v; return this; }
        public Builder initialGrassCells(int v) { initialGrassCells = v; return this; }

        public SimulationConfig build() { return new SimulationConfig(this); }
    }
}