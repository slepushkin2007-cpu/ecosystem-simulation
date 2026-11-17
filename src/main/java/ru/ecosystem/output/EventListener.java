package ru.ecosystem.output;

import ru.ecosystem.model.event.Event;

/**
 * Слушатель событий механики. Механика о нём не знает.
 */
public interface EventListener {
    void onEvent(Event event);
}