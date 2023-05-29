package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MyAppException;
import ru.yandex.practicum.filmorate.model.enumeration.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@Slf4j
@Service("jdbcMpaService")
public class JdbcMpaService implements MpaService {
    private final MpaRepository mpaRepository;

    public JdbcMpaService(@Qualifier("jdbcMpaRepository") MpaRepository mpaRepository) {
        this.mpaRepository = mpaRepository;
    }

    @Override
    public Collection<Mpa> findAll() {
        return mpaRepository.findAll();
    }

    @Override
    public Mpa findById(Long id) {
        return mpaRepository.findById(id).orElseThrow(() ->
                new MyAppException("404", String.format("Рейтинг с id %d не найден.", id), HttpStatus.NOT_FOUND));
    }

    @Override
    public Mpa create(Mpa type) {
        return mpaRepository.create(type);
    }

    @Override
    public Mpa update(Mpa type) {
        return mpaRepository.update(type);
    }

    @Override
    public void delete(Long id) {
        mpaRepository.delete(id);
    }
}
