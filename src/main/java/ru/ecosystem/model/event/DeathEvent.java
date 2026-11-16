package ru.ecosystem.model.event;

import ru.ecosystem.model.Position;

public record DeathEvent(int tick, String type, Position where, DeathCause cause) implements Event {}