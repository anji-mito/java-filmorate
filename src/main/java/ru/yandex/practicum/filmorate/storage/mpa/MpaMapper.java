package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.HashMap;
import java.util.Map;

public class MpaMapper {
    public static Map<String, ?> toMap(Mpa mpa) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", mpa.getName());
        return values;
    }
}
