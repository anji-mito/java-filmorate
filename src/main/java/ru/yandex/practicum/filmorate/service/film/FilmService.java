package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.FilmsGenresDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.FilmsGenresStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final FilmsGenresStorage filmsGenresDbStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService, FilmsGenresDbStorage filmsGenresDbStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.filmsGenresDbStorage = filmsGenresDbStorage;
    }

    public Film create(Film film) {
        return getFilm(filmStorage.create(film));
    }

    public Film update(Film film) throws IllegalStateException {
        if (filmStorage.findFilm(film.getId()).isEmpty()) {
            throw new IllegalStateException("Not found such a user");
        }
        if (filmStorage.update(film).isPresent()) {
            return (filmStorage.update(film).get());
        } else {
            throw new IllegalStateException("Not found such a user");
        }
    }

    public List<Film> getAllFilms() {
        return filmStorage.findAll();
    }

    public void addLike(Long filmId, Long userId) throws IllegalStateException {
        getFilm(filmId);
        userService.getUser(userId);
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) throws IllegalStateException {
        getFilm(filmId);
        userService.getUser(userId);
        filmStorage.addLike(filmId, userId);
    }

     public List<Film> getPopular(int count) {
          return filmStorage.getPopular(count);
    }

    public Film getFilm(Long id) {
        Film foundFilm = filmStorage.findFilm(id)
                .orElseThrow(() -> new FilmNotFoundException("Film is not found"));
        List<Genre> genres = filmsGenresDbStorage.getGenresOfFilm(id);
        foundFilm.setGenres(genres);
        return foundFilm;
    }
}
