package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.enumeration.Mpa;

public interface MpaRepository extends CrudRepository<Mpa> {
    boolean existsById(long id);
}
