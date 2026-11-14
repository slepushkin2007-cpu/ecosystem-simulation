package ru.ecosystem.model.exception;

/**
 * Исключение при недопустимой конфигурации симуляции.
 */
public class InvalidConfigException extends IllegalArgumentException {
    public InvalidConfigException(String message) {
        super(message);
    }
}