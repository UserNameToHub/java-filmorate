package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public Map<Integer, User> findAll() {
        User user = new User("sa@gmail.com", "log", "log",
                LocalDate.of(2007, 02, 07));
        users.put(user.getId(), user);
        return users;
    }

    @PostMapping("/user")
    public User create(@Valid @RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/user")
    public User update(@Valid @RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }
}
