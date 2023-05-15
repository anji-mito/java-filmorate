package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    ResponseEntity<User> create(User user);
    ResponseEntity<User> update(User user);
    void remove(User user);
    ResponseEntity<List<User>> getAllUsers();
    void addFriend(long userId, long friendId);
}
