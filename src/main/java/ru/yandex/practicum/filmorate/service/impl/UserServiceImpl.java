package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MyAppException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.InMemoryUserRepository;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final InMemoryUserRepository userRepository;

    private Long idU = 1L;

    @Autowired
    public UserServiceImpl(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Collection<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new MyAppException("404", String.format("Пользователь с id %d не найден", id), HttpStatus.NOT_FOUND));
    }

    @Override
    public User create(User type) {
        type.setId(idU++);
        if (userRepository.findById(type.getId()).isPresent()) {
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
        checkUserById(type.getId());
        return userRepository.update(type);
    }

    @Override
    public void delete(Long id) {
        checkUserById(id);
        userRepository.delete(id);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        checkPathVarsForNull(id, friendId);
        checkUserById(id, friendId);
        userRepository.addFriend(id, friendId);
        log.info("Пользователь с id {} был добавлен в друзья.", friendId);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        checkPathVarsForNull(id, friendId);
        checkUserById(id, friendId);
        userRepository.addFriend(id, friendId);
        log.info("Пользователь с id {} был удален из друзей.", friendId);
    }

    @Override
    public List<User> findAllFriendsForUser(Long id) {
        checkPathVarsForNull(id);
        checkUserById(id);
        return userRepository.findAllFriends(id);
    }

    @Override
    public List<User> findCommonFriends(Long id, Long otherId) {
        checkPathVarsForNull(id, otherId);
        checkUserById(id, otherId);
        return userRepository.findCommonFriends(id, otherId);
    }

    private void checkUserById(Long... ids) {
        for (Long id : ids) {
            userRepository.findById(id).orElseThrow(() ->
                    new MyAppException("404", String.format("Пользователь с id %d не найден", id),
                            HttpStatus.NOT_FOUND));
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