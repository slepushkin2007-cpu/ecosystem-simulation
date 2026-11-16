package ru.ecosystem.model.event;

import ru.ecosystem.model.Position;

public record MoveEvent(int tick, String type, Position from, Position to) implements Event {}