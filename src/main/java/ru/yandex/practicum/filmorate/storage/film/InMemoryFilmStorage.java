package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
    public class InMemoryFilmStorage implements FilmStorage {
    private final List<Film> films = new ArrayList<>();
    private long id;

    @Override
    public Film getFilm(Long id) {
        return films.stream()
                .filter(film -> film.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found film"));
    }

    @Override
    public ResponseEntity<Film> create(Film film) {
        film.setId(++id);
        films.add(film);
        log.info("Фильм успешно добавлен. Текущее количество фильмов: " + films.size());
        return ResponseEntity.ok(film);
    }

    @Override
    public ResponseEntity<Film> update(Film film) {
        Film foundFilm = films.stream()
                .filter(film1 -> film1.getId() == film.getId())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Not found film"));
        films.remove(foundFilm);
        films.add(film);
        log.info("Фильм успешно обновлен.");
        return ResponseEntity.ok(film);
    }

    @Override
    public void remove(Film film) {
        films.remove(film);
    }

    @Override
    public ResponseEntity<List<Film>> getAll() {
        return ResponseEntity.ok(films);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        Film film = getFilm(filmId);
        var likes = film.getLikes();
        likes.add(userId);
        film.setLikes(likes);
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        Film film = getFilm(filmId);
        var likes = film.getLikes();
        if (likes.contains(userId)) {
            likes.remove(userId);
            film.setLikes(likes);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found such a user");
        }
    }

    @Override
    public List<Film> getPopular(int count) {
         var sorted = new ArrayList<>(List.copyOf(films));
         sorted.sort((o1, o2) -> o2.getLikes().size() - (o1.getLikes().size()));
         if (count > sorted.size()) {
             count = sorted.size();
         }
         return sorted.subList(0, count);
    }
}
