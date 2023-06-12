package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        checkIfNameIsNotEmpty(user);
        return userStorage.create(user);
    }

    public User update(User user) throws IllegalStateException {
        long userId = user.getId();
        checkIfNameIsNotEmpty(user);
        if (userStorage.findUser(userId).isEmpty()) {
            throw new IllegalStateException("Not found such a user");
        }
        return userStorage.update(user);
    }

    public List<Optional<User>> getAllUsers() {
        return userStorage.findAllUsers();
    }

    public User getUser(Long userId) {
        if (userStorage.findUser(userId).isPresent()) {
            return userStorage.findUser(userId).get();
        } else {
            throw new IllegalStateException("Not found such a user");
        }
    }

    public List<User> getFriends(Long userId) {
        List<Long> friends = userStorage.getFriends(userId);
        List<User> result = new ArrayList<>();
        for (Long friendId : friends) {
            result.add(getUser(friendId));
        }
        return result;
    }

    public void addFriend(long id, long friendId) {
        if (getUser(id) != null && getUser(friendId) != null) {
            userStorage.addFriend(id, friendId);
        }
    }

    public void removeFriend(long userId, long friendId) {
        if (userStorage.findUser(userId).isPresent() && userStorage.findUser(friendId).isPresent()) {
            userStorage.removeFriend(userId, friendId);
        }
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        List<User> commonFriends = new ArrayList<>();
        var friends = getFriends(friendId);
        for (User user : getFriends(userId)) {
            if (friends.contains(user)) {
                commonFriends.add(user);
            }
        }
        return commonFriends;
    }

    private void checkIfNameIsNotEmpty(User user) {
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
    }
}