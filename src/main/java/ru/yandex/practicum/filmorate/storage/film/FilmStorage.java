package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmStorage {
    ResponseEntity<Film> create(Film film);
    ResponseEntity<Film> update(Film film);
    void remove(Film film);
    ResponseEntity<List<Film>> getAll();
}
