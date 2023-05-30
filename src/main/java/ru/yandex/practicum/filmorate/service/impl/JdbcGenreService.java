package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MyAppException;
import ru.yandex.practicum.filmorate.model.enumeration.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Slf4j
@Service("jdbcGenreService")
public class JdbcGenreService implements GenreService {
    private final GenreRepository genreRepository;

    public JdbcGenreService(@Qualifier("jdbcGenreRepository") GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Collection<Genre> findAll() {
        log.info("Запрос на получение списка всех жанров из базы.");
        return genreRepository.findAll();
    }

    @Override
    public Genre findById(Long id) {
        log.info("Запрос на получение жанра c id {}.", id);
        return genreRepository.findById(id).orElseThrow(() ->
                new MyAppException("404", String.format("Жанр с id %d не найден.", id), HttpStatus.NOT_FOUND));
    }

    @Override
    public Genre create(Genre type) {
        log.info("Запрос на создание жанра.");
        return genreRepository.create(type);
    }

    @Override
    public Genre update(Genre type) {
        log.info("Запрос на обновление жанра.");
        checkGenreById(type.getId());
        log.info("Информация о жанре с id {} была обновлена.");
        return genreRepository.update(type);
    }

    @Override
    public void delete(Long id) {
        log.info("Запрос на удаление жанра.");
        checkGenreById(id);
        log.info("Жанр с id {} был удален.");
        genreRepository.delete(id);
    }

    private void checkGenreById(long id) {
        if (!genreRepository.existsById(id)) {
            throw new MyAppException("404", String.format("Жанр с id %d или не найден.", id),
                    HttpStatus.NOT_FOUND);
        }
    }
}
