package ru.ecosystem.model.event;

/**
 * Базовое событие поколения.
 */
public sealed interface Event permits BirthEvent, DeathEvent, EatEvent, MoveEvent, GrassGrowthEvent {
    int tick();
}