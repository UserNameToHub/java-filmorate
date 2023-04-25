package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.CrudRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements CrudRepository<User> {
    private static final Map<Integer, User> IN_MEMORY_DB = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return IN_MEMORY_DB.values();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return IN_MEMORY_DB.entrySet().stream()
                .filter(k -> k.getKey() == id)
                .map(k -> k.getValue())
                .findFirst();
    }

    @Override
    public Optional<User> create(User type) {
        IN_MEMORY_DB.put(type.getId(), type);
        return Optional.ofNullable(type);
    }

    @Override
    public Optional<User> update(User type) {
        IN_MEMORY_DB.put(type.getId(), type);
        return Optional.ofNullable(type);
    }

    @Override
    public void delete(Integer id) {
        IN_MEMORY_DB.remove(id);
    }
}
