package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MyAppException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service("jdbcFilmService")
public class JdbcFilmService implements FilmService {
    private final FilmRepository filmRepository;

    public JdbcFilmService(@Qualifier("jdbcFilmRepository") FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public Collection<Film> findAll() {
        log.info("Запрос на получение списка всех фильмов из базы.");
        return filmRepository.findAll();
    }

    @Override
    public Film findById(Long id) {
        log.info("Запрос на получение фильма c id {}.", id);
        return filmRepository.findById(id).orElseThrow(() ->
                new MyAppException("404", String.format("Фильм с id %d не найден.", id), HttpStatus.NOT_FOUND));
    }

    @Override
    public Film create(Film type) {
        log.info("Запрос на создание фильма.");
        return filmRepository.create(type);
    }

    @Override
    public Film update(Film type) {
        log.info("Запрос на обновление фильма.");
        return filmRepository.update(type);
    }

    @Override
    public void delete(Long id) {
        log.info("Запрос на удаление фильма.");
        filmRepository.delete(id);
    }

    @Override
    public void addLike(Long id, Long userId) {
        log.info("Запрос на добавление лайка.");
        filmRepository.addLike(id, userId);
        log.info("Пользователь с id {} поставил лайк фильму с id {}.", userId, id);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        log.info("Запрос на удаление лайка.");
        filmRepository.deleteLike(id, userId);
        log.info("Пользователь с id {} удалил лайк фильму с id {}.", userId, id);
    }

    @Override
    public List<Film> findFirstCountFilms(Integer count) {
        log.info("Запрос на получение списока из первых {} фильмов по количеству лайков.", count);
        return filmRepository.findFirstCountFilms(count);
    }
}