package ru.ecosystem.output;

import ru.ecosystem.model.event.Event;

public interface EventListener {
    void onEvent(Event event);
}