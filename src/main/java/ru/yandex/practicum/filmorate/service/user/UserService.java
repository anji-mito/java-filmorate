package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public List<User> getAllUsers() {
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
        var friends = getUser(userId).getFriends();
        List<User> result = new ArrayList<>();
        for (long friendId : friends) {
            result.add(getUser(friendId));
        }
        return result;
    }

    public void addFriend(long id, long friendId) {
        User user = getUser(id);
        User friend = getUser(friendId);
        user.friends.add(friend.getId());
        friend.friends.add(user.getId());
    }

    public void removeFriend(long userId, long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        user.friends.remove(friendId);
        friend.friends.remove(userId);
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
}