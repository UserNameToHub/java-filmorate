package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MyAppException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.InMemoryUserRepository;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Qualifier("userDbRepository")
    private final InMemoryUserRepository userRepository;

    private Long idU = 1L;

    @Override
    public Collection<User> findAll() {
        log.info("Запрос на получение списка всех пользователей из базы.");
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        log.info("Запрос на получение пользователя c id {}.", id);
        return userRepository.findById(id).orElseThrow(() ->
                new MyAppException("404", String.format("Пользователь с id %d не найден", id), HttpStatus.NOT_FOUND));
    }

    @Override
    public User create(User type) {
        log.info("Запрос на создание пользователя.");
        type.setId(idU++);
        if (userRepository.existsById(type.getId())) {
            throw new MyAppException("400", String.format("Пользователь с id %d уже есть в базе.", type.getId()),
                    HttpStatus.BAD_REQUEST);
        }

        if (!checkUserName(type.getName())) {
            type.setName(type.getLogin());
        }

        return userRepository.create(type);
    }

    @Override
    public User update(User type) {
        log.info("Запрос на обновление пользователя.");
        checkUserById(type.getId());
        return userRepository.update(type);
    }

    @Override
    public void delete(Long id) {
        log.info("Запрос на удаление пользователя.");
        checkUserById(id);
        userRepository.delete(id);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        log.info("Запрос на добавление друга.");
        checkPathVarsForNull(id, friendId);
        checkUserById(id, friendId);
        userRepository.addFriend(id, friendId);
        log.info("Пользователь с id {} был добавлен в друзья.", friendId);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        log.info("Запрос на удаление друга.");
        checkPathVarsForNull(id, friendId);
        checkUserById(id, friendId);
        userRepository.addFriend(id, friendId);
        log.info("Пользователь с id {} был удален из друзей.", friendId);
    }

    @Override
    public List<User> findAllFriendsForUser(Long id) {
        log.info("Запрос на список друзей пользователя с id {}.", id);
        checkPathVarsForNull(id);
        checkUserById(id);
        return userRepository.findAllFriends(id);
    }

    @Override
    public List<User> findCommonFriends(Long id, Long otherId) {
        log.info("Запрос на список друзей пользователя с id {}, общих с пользователем с id {}.", id, otherId);
        checkPathVarsForNull(id, otherId);
        checkUserById(id, otherId);
        return userRepository.findCommonFriends(id, otherId);
    }

    public void checkUserById(Long... ids) {
        for (Long id : ids) {
            if (!userRepository.existsById(id)) {
                throw new MyAppException("404", String.format("Пользователь с id %d не найден", id),
                        HttpStatus.NOT_FOUND);
            }
        }
    }

    private void checkPathVarsForNull(Long... pathVars) {
        boolean res = Arrays.stream(pathVars).allMatch(Objects::nonNull);
        if (!res) {
            throw new MyAppException("400", String.format("Переменные пути не могут быть пустыми."),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private boolean checkUserName(String name) {
        return StringUtils.isNotBlank(name);
    }
}