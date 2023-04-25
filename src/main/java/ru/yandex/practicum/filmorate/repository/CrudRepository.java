package ru.yandex.practicum.filmorate.repository;

import java.util.Collection;
import java.util.Optional;

public interface CrudRepository<T> {
    Collection<T> findAll();
    Optional<T> findById(Integer id);
    Optional<T> create(T type);
    Optional<T> update(T type);
    void delete(Integer id);
}
