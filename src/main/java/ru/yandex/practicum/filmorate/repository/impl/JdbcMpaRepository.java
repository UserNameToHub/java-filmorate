package ru.yandex.practicum.filmorate.repository.impl;

import ru.yandex.practicum.filmorate.model.enumeration.Mpa;

import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.Collection;
import java.util.Optional;

public class JdbcMpaRepository implements MpaRepository {
    @Override
    public Collection<Mpa> findAll() {
        return null;
    }

    @Override
    public Optional<Mpa> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Mpa create(Mpa type) {
        return null;
    }

    @Override
    public Mpa update(Mpa type) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
