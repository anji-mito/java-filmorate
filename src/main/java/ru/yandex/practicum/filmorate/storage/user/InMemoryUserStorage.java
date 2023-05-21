package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final List<User> users = new ArrayList<>();
    private long id;

    @Override
    public ResponseEntity<User> create(User user) {
        checkIfNameIsNotNull(user);
        user.setId(++id);
        users.add(user);
        log.info("Пользователь успешно добавлен. Текущее количество пользователей: " + users.size());
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<User> update(User user) {
        checkIfNameIsNotNull(user);
        User foundUser = users.stream().filter(user1 -> user1.getId() == user.getId()).findFirst()
                .orElseThrow(() -> new IllegalStateException("Not found user"));
        users.remove(foundUser);
        users.add(user);
        log.info("Пользователь успешно обновлен.");
        return ResponseEntity.ok(user);
    }

    @Override
    public void remove(User user) {
        users.remove(user);
    }

    @Override
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(users);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        user.friends.add(friend.getId());
        friend.friends.add(user.getId());
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        user.friends.remove(friendId);
        friend.friends.remove(userId);
    }

    @Override
    public Set<Long> getFriends(long userId) {
        return getUser(userId).getFriends();
    }

    @Override
    public User getUser(Long userId) {
        return users.stream().filter(user1 -> user1.getId() == userId).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found user"));
    }

    private void checkIfNameIsNotNull(User user) {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
    }
}
