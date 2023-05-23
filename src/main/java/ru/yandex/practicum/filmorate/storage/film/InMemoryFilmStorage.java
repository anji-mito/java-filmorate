package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final List<Film> films = new ArrayList<>();
    private long id;

    @Override
    public Optional<Film> findFilm(Long id) {
        return films.stream().filter(film -> film.getId() == id).findFirst();
    }

    @Override
    public Film create(Film film) {
        film.setId(++id);
        films.add(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Film foundFilm = films.stream().filter(film1 -> film1.getId() == film.getId()).findFirst()
                .orElseThrow(() -> new IllegalStateException("Not found film"));
        films.remove(foundFilm);
        films.add(film);
        return film;
    }

    @Override
    public void remove(Film film) {
        films.remove(film);
    }

    @Override
    public List<Film> findAll() {
        return films;
    }

}
