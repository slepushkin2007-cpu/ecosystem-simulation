package ru.ecosystem.model.event;

import ru.ecosystem.model.Position;

public record GrassGrowthEvent(int tick, Position where, int amount) implements Event {}