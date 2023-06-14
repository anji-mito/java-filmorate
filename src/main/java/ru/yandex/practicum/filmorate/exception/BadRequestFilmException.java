package ru.yandex.practicum.filmorate.exception;

public class BadRequestFilmException extends RuntimeException {
    public BadRequestFilmException(String message) {
        super(message);
    }
}
