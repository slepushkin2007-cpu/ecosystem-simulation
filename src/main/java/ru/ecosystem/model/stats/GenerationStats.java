package ru.ecosystem.model.stats;

/**
 * Снимок статистики по одному поколению.
 */
public record GenerationStats(
        int tick,
        int grassTotalLevel,
        int herbivores,
        int predators,
        int births,
        int deaths
) {}