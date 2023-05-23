package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Optional<Film> findFilm(Long id);

    Film create(Film film);

    Film update(Film film);

    void remove(Film film);

    List<Film> findAll();

}
