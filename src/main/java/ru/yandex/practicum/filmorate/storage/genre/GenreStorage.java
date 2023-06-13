package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    Optional<Genre> findGenre(Long id);

    Genre create(Genre genre);

    Genre update(Genre genre);

    void remove(Genre genre);

    List<Genre> findAll();
}
