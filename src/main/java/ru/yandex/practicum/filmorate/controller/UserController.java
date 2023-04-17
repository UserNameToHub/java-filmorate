package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @GetMapping
    public List<User> findAll() {
        log.info("Запрос на получение всех пользователей из базы.");
        return users.values().stream().collect(Collectors.toList());
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        user.setId(id++);
        if (users.containsKey(user.getId())) {
            log.info("Пользователь с id {} уже есть в базе.", user.getId());
            return user;
        }

        if (!checkUserName(user.getName())) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        log.info("Пользователь был добавлен в базу.");
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователя с id {} еще нет в базе.", user.getId());
            throw new ValidationException(String.format("Пользователь c id %d еще не добавлен в базу.", user.getId()));
        }
        users.put(user.getId(), user);
        log.info("Пользователь с id {} был изменен в базе.", user.getId());
        return user;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationException handleException(MethodArgumentNotValidException e) {
        return new ValidationException(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ValidationException.class)
    public void handleException(ValidationException e) {
        throw new ValidationException(e.getMessage());
    }

    private boolean checkUserName(String name) {
        return StringUtils.isNotBlank(name);
    }
}