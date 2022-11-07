package ru.yandex.practicum.filmorate.services;

public class ValidationException extends RuntimeException {
    public ValidationException(final String message) {
        super(message);
    }
}
