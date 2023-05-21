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

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
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

    public void addLike(Long filmId, Long userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getPopular(count);
    }

    public Film getFilm(Long id) {
        return filmStorage.getFilm(id);
    }
}
