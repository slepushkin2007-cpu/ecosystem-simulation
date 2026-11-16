package ru.ecosystem.model.event;

import ru.ecosystem.model.Position;

public record BirthEvent(int tick, String type, Position parent, Position child) implements Event {}