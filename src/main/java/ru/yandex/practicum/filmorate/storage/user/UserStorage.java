package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User create(User user);

    User update(User user);

    void remove(User user);

    List<Optional<User>> findAllUsers();

    Optional<User> findUser(Long userId);

    List<Long> getFriends(Long id);

    void addFriend(long id, long friendId);

    void removeFriend(long id, long friendId);
}
