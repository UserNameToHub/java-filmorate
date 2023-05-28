package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.enumeration.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Slf4j
@Service("jdbcGenreService")
public class JdbcGenreService implements GenreService {
    @Override
    public Collection<Genre> findAll() {
        return null;
    }

    @Override
    public Genre findById(Long id) {
        return null;
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
