package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping("/users")
    public Map<Long, User> findAll() {
        return users;
    }

    @PostMapping("/user")
    public User create(@RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/user")
    public User update(@RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }
}
