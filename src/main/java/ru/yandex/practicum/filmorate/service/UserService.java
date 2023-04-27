package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService extends CrudService<User> {
    void addFriend(Long id, Long friendId);
    void deleteFriend(Long id, Long friendId);
    List<User> findAllFriendsForUser(Long id);
    List<User> findCommonFriends(Long id, Long otherId);
}
