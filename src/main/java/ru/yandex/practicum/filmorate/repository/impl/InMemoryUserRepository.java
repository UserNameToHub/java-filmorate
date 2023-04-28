package ru.yandex.practicum.filmorate.repository.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Map<Long, User> IN_MEMORY_DB = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return IN_MEMORY_DB.values();
    }

    @Override
    public Optional<User> findById(Long id) {
        return IN_MEMORY_DB.entrySet().stream()
                .filter(k -> k.getKey() == id)
                .map(Map.Entry::getValue)
                .findFirst();
    }

    @Override
    public User create(User type) {
        IN_MEMORY_DB.put(type.getId(), type);
        return type;
    }

    @Override
    public User update(User type) {
        IN_MEMORY_DB.put(type.getId(), type);
        return type;
    }

    @Override
    public void delete(Long id) {
        IN_MEMORY_DB.remove(id);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        IN_MEMORY_DB.get(id).getFriends().add(friendId);
        IN_MEMORY_DB.get(friendId).getFriends().add(id);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        IN_MEMORY_DB.get(id).getFriends().remove(friendId);
        IN_MEMORY_DB.get(friendId).getFriends().remove(id);
    }

    @Override
    public List<User> findAllFriends(Long id) {
        Set<Long> friends = IN_MEMORY_DB.get(id).getFriends();
        return getFriendsList(friends);
    }

    @Override
    public List<User> findCommonFriends(Long id, Long otherId) {
        Set<Long> friendsThisUser = IN_MEMORY_DB.get(id).getFriends();
        Set<Long> friendsOtherUser = IN_MEMORY_DB.get(otherId).getFriends();

        Collection<Long> commonFriends = CollectionUtils.intersection(friendsThisUser, friendsOtherUser);

        return getFriendsList(commonFriends);
    }

    private List<User> getFriendsList(Collection<Long> friendsId) {
        return friendsId.stream()
                .map(IN_MEMORY_DB::get)
                .collect(Collectors.toList());
    }

    public void clearDB() {
        IN_MEMORY_DB.clear();
    }
}
