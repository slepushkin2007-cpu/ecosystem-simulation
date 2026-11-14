package ru.ecosystem.model.exception;

/**
 * Исключение при обращении к клетке за пределами решётки.
 */
public class OutOfGridException extends RuntimeException {
    public OutOfGridException(String message) {
        super(message);
    }
}