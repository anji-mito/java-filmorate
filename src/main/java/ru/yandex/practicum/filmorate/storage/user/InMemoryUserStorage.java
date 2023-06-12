package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final List<User> users = new ArrayList<>();
    private long id;

    @Override
    public User create(User user) {
        checkIfNameIsNotNull(user);
        user.setId(++id);
        users.add(user);
        return user;
    }

    @Override
    public User update(User user) {
        checkIfNameIsNotNull(user);
        User foundUser = users.stream().filter(user1 -> user1.getId() == user.getId()).findFirst()
                .orElseThrow(() -> new IllegalStateException("Not found user"));
        users.remove(foundUser);
        users.add(user);
        return (user);
    }

    @Override
    public void remove(User user) {
        users.remove(user);
    }

    @Override
    public List<User> findAllUsers() {
        return users;
    }

    @Override
    public Optional<User> findUser(Long userId) {
        return users.stream().filter(user1 -> user1.getId() == userId).findFirst();
    }

    private void checkIfNameIsNotNull(User user) {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
    }
}
