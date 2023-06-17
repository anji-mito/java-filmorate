package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {
    Optional<Mpa> findMpa(Long id);

    Mpa create(Mpa mpa);

    Mpa update(Mpa mpa);

    void remove(Mpa mpa);

    List<Mpa> findAll();

}
