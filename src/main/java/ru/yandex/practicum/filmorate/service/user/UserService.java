package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity<User> create(User user) {
        return userStorage.create(user);
    }

    public ResponseEntity<User> update(User user) {
        return userStorage.update(user);
    }

    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userStorage.getAllUsers().getBody());
    }

    public User getUser(Long userId) {
        return userStorage.getUser(userId);
    }

    public List<User> getFriends(Long userId) {
        var friends = userStorage.getFriends(userId);
        List<User> result = new ArrayList<>();
        for (long friendId: friends) {
            result.add(getUser(friendId));
        }
        return result;
    }

    public void addFriend(long id, long friendId) {
        userStorage.addFriend(id, friendId);
    }

    public void removeFriend(long userId, long friendId) {
        userStorage.removeFriend(userId, friendId);
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        List<User> commonFriends = new ArrayList<>();
        var friends = userStorage.getFriends(friendId);
        for (Long id : userStorage.getFriends(userId)) {
            if(friends.contains(id)){
                commonFriends.add(getUser(id));
            }
        }
        return commonFriends;
    }
}