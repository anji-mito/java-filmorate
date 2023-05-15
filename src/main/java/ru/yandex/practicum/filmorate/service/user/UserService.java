package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    @Autowired
    public UserService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public ResponseEntity<User> create(User user) {
        return userStorage.create(user);
    }

    public ResponseEntity<User> update(User user) {
        return userStorage.update(user);
    }

    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userStorage.getAllUsers().getBody());
    }

    public void addFriend(long id, long friendId) {
        userStorage.addFriend(id, friendId);
    }
}
