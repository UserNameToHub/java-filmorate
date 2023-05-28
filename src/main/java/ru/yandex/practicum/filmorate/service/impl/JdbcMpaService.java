package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.enumeration.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@Slf4j
@Service("jdbcMpaService")
public class JdbcMpaService implements MpaService {
    @Override
    public Collection<Mpa> findAll() {
        return null;
    }

    @Override
    public Mpa findById(Long id) {
        return null;
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
