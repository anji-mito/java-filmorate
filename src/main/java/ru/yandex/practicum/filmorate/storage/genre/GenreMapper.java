package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashMap;
import java.util.Map;

public class GenreMapper {
    public static Map<String, ?> toMap(Genre genre) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", genre.getName());
        return values;
    }
}
