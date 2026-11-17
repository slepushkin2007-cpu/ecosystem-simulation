package ru.ecosystem.output;

import ru.ecosystem.model.event.*;

/**
 * Печатает события в консоль. Единственное место в проекте, где допустим System.out.
 */
public class ConsolePrinter implements EventListener {
    @Override
    public void onEvent(Event e) {
        if (e instanceof GrassGrowthEvent g) {
            System.out.printf("[tick %d] Трава +%d в %s%n", g.tick(), g.amount(), g.where());
        } else if (e instanceof EatEvent eat) {
            System.out.printf("[tick %d] %s съел %s в %s%n", eat.tick(), eat.eaterType(), eat.foodType(), eat.where());
        } else if (e instanceof MoveEvent m) {
            System.out.printf("[tick %d] %s %s -> %s%n", m.tick(), m.type(), m.from(), m.to());
        } else if (e instanceof BirthEvent b) {
            System.out.printf("[tick %d] Рождение %s у %s%n", b.tick(), b.type(), b.parent());
        } else if (e instanceof DeathEvent d) {
            System.out.printf("[tick %d] Смерть %s в %s (причина: %s)%n", d.tick(), d.type(), d.where(), d.cause());
        }
    }
}