package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }
    public ResponseEntity<Film> create(Film film) {
        return filmStorage.create(film);
    }

    public ResponseEntity<Film> update(Film film) {
        return filmStorage.update(film);
    }

    public ResponseEntity<List<Film>> getAllFilms() {
        return ResponseEntity.ok(filmStorage.getAll().getBody());
    }
}
