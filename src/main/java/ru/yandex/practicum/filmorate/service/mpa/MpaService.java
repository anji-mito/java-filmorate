package ru.yandex.practicum.filmorate.service.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;
import java.util.Optional;

@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Optional<Mpa> getMpa(long id) {
        Optional<Mpa> foundMpa = mpaStorage.findMpa(id);
        if (foundMpa.isPresent()) {
            return foundMpa;
        } else {
            throw new IllegalStateException("Mpa is not found");
        }
    }

    public List<Optional<Mpa>> getAll() {
        return mpaStorage.findAll();
    }
}
