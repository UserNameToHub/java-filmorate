package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserRepository extends CrudRepository<User> {
    void addFriend(Long id, Long friendId);
    void deleteFriend(Long id, Long friendId);
    List<User> findAllFriends(Long id);
    List<User> findCommonFriends(Long id, Long otherId);
}
