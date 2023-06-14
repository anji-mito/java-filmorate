package ru.yandex.practicum.filmorate.storage.FilmsGenres;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmsGenresStorage {
    List<Genre> getGenresOfFilm(long id);
}
