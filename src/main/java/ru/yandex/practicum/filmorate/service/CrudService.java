package ru.yandex.practicum.filmorate.service;

import java.util.Collection;

public interface CrudService<T> {
    Collection<T> findAll();

    T findById(Long id);

    T create(T type);

    T update(T type);

    void delete(Long id);
}
