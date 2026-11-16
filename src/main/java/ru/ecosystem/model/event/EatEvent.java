package ru.ecosystem.model.event;

import ru.ecosystem.model.Position;

public record EatEvent(int tick, Position where, String eaterType, String foodType) implements Event {}