package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) throws IllegalStateException {
        return filmStorage.update(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.findAll();
    }

    public void addLike(Long filmId, Long userId) throws IllegalStateException {
        if (userService.getUser(userId) == null) {
            throw new IllegalStateException("Not found such a user");
        }
        Optional<Film> foundFilm = filmStorage.findFilm(filmId);
        if (foundFilm.isPresent()) {
            Film film = foundFilm.get();
            var likes = film.getLikes();
            likes.add(userId);
            film.setLikes(likes);
        } else {
            throw new IllegalStateException("Not found such a film");
        }
    }

    public void removeLike(Long filmId, Long userId) throws IllegalStateException {
        Optional<Film> foundFilm = filmStorage.findFilm(filmId);
        if (foundFilm.isPresent()) {
            Film film = foundFilm.get();
            var likes = film.getLikes();
            if (likes.contains(userId)) {
                likes.remove(userId);
                film.setLikes(likes);
            } else {
                throw new IllegalStateException("Not found such a user");
            }
        } else {
            throw new IllegalStateException("Not found such a film");
        }
    }

    public List<Film> getPopular(int count) {
        var sorted = new ArrayList<>(List.copyOf(filmStorage.findAll()));
        sorted.sort((o1, o2) -> o2.getLikes().size() - (o1.getLikes().size()));
        if (count > sorted.size()) {
            count = sorted.size();
        }
        return sorted.subList(0, count);
    }

    public Film getFilm(Long id) throws IllegalStateException {
        Optional<Film> foundFilm = filmStorage.findFilm(id);
        if (foundFilm.isPresent()) {
            return foundFilm.get();
        } else {
            throw new IllegalStateException("Film not found");
        }
    }
}
