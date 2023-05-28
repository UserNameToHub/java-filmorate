package ru.yandex.practicum.filmorate.repository.impl;

import ru.yandex.practicum.filmorate.model.enumeration.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.Collection;
import java.util.Optional;

public class JdbcGenreRepository implements GenreRepository {
    @Override
    public Collection<Genre> findAll() {
        return null;
    }

    @Override
    public Optional<Genre> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Genre create(Genre type) {
        return null;
    }

    @Override
    public Genre update(Genre type) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
