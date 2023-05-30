package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.enumeration.Genre;

public interface GenreRepository extends CrudRepository<Genre> {
    boolean existsById(long id);
}
