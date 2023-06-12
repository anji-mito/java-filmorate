package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class Mpa {
    private int id;
    private String name;

    public Map<String, ?> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", getName());
        return values;
    }
}
