package ru.ecosystem.model.stats;

public record GenerationStats(
        int tick,
        int grassTotalLevel,
        int herbivores,
        int predators,
        int births,
        int deaths
) {}