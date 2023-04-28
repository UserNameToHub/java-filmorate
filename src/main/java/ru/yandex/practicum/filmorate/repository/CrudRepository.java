package ru.yandex.practicum.filmorate.repository;

import java.util.Collection;
import java.util.Optional;

public interface CrudRepository<T> {

    Collection<T> findAll();

    Optional<T> findById(Long id);

    T create(T type);

    T update(T type);

    void delete(Long id);
}
