package ru.ecosystem.model;

import ru.ecosystem.model.event.Event;
import ru.ecosystem.model.stats.GenerationStats;

import java.util.List;

/**
 * Результат одного шага поколения.
 */
public record GenerationResult(
        int tick,
        List<Event> events,
        GenerationStats stats,
        boolean finished,
        String finishReason
) {}