package ru.yandex.practicum.filmorate.service.genres;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Optional<Genre> getGenre(long id) {
        Optional<Genre> foundGenre = genreStorage.findGenre(id);
        if (foundGenre.isPresent()) {
            return foundGenre;
        } else {
            throw new IllegalStateException("Genre is not found");
        }
    }

    public List<Optional<Genre>> getAll() {
        return genreStorage.findAll();
    }

    public Genre create(Genre genre) {
        return genreStorage.create(genre);
    }

    public Genre update(Genre genre) throws IllegalStateException {
        if (genreStorage.findGenre((long) genre.getId()).isEmpty()) {
            throw new IllegalStateException("genre is not found");
        }
        return genreStorage.update(genre);
    }
}
