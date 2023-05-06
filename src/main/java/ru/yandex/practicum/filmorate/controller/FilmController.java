package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/films")
public class FilmController {
    private final List<Film> films = new ArrayList<>();
    private long id;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        film.setId(++id);
        films.add(film);
        log.info("Фильм успешно добавлен. Текущее количество фильмов: " + films.size());
        return ResponseEntity.ok(film);
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        Film foundFilm = films.stream()
                .filter(film1 -> film1.getId() == film.getId())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Not found film"));
        films.remove(foundFilm);
        films.add(film);
        log.info("Фильм успешно обновлен.");
        return ResponseEntity.ok(film);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAll() {
        return ResponseEntity.ok(films);
    }
}
