package ru.ecosystem.model.event;

public sealed interface Event permits BirthEvent, DeathEvent, EatEvent, MoveEvent, GrassGrowthEvent {
    int tick();
}