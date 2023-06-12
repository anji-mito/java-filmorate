package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Optional<Film> findFilm(Long id);

    long create(Film film);

    Optional<Film> update(Film film);

    void remove(Film film);

    List<Optional<Film>> findAll();

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

}
