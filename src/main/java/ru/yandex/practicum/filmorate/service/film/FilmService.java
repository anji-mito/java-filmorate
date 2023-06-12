package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.FilmsGenresDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.FilmsGenresStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<Optional<Film>> getAllFilms() {
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

    public List<Optional<Film>> getPopular(int count) {
        var sorted = new ArrayList<>(List.copyOf(filmStorage.findAll()));
        sorted.sort((o1, o2) -> o2.get().getRate() - (o1.get().getRate()));
        if (count > sorted.size()) {
            count = sorted.size();
        }
        return sorted.subList(0, count);
    }

    public Film getFilm(Long id) throws IllegalStateException {
        Optional<Film> foundFilm = filmStorage.findFilm(id);
        if (foundFilm.isPresent()) {
            var r = filmsGenresDbStorage.getGenresOfFilm(id);
            List<Genre> genres = new ArrayList<>();
            for (Optional<Genre> genre : r) {
                genre.ifPresent(genres::add);
            }
            Film film = foundFilm.get();
            film.setGenres(genres);
            return film;
        } else {
            throw new IllegalStateException("Film is not found");
        }
    }
}
