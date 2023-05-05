package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final List<User> users = new ArrayList<>();
    private long id;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(++id);
        users.add(user);
        log.info("Пользователь успешно добавлен. Текущее количество пользователей: " + users.size());
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        var userToUpdate = users.stream()
                                 .filter(user1 -> user1.getId() == user.getId())
                                 .findFirst();
        if (userToUpdate.isPresent()) {
            users.remove(userToUpdate.get());
            users.add(user);
            log.info("Пользователь успешно обновлен.");
            return ResponseEntity.ok(user);
        } else {
            log.info("Пользователь не был найден: " + user.getLogin());
            return (ResponseEntity<User>) ResponseEntity.badRequest();
        }

    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(users);
    }
}
