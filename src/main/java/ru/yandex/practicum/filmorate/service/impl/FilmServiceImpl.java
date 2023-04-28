package ru.yandex.practicum.filmorate.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MyAppException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.impl.InMemoryFilmRepository;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    private final InMemoryFilmRepository filmRepository;

    private final UserServiceImpl userService;

    private Long idF = 1l;

    @Autowired
    public FilmServiceImpl(InMemoryFilmRepository filmRepository, UserServiceImpl userService) {
        this.filmRepository = filmRepository;
        this.userService = userService;
    }

    @Override
    public Collection<Film> findAll() {
        return filmRepository.findAll();
    }

    @Override
    public Film findById(Long id) {
        return filmRepository.findById(id).orElseThrow(() ->
                new MyAppException("404", String.format("Фильм с id %d не найден.", id), HttpStatus.NOT_FOUND));
    }

    @Override
    public Film create(Film type) {
        type.setId(idF++);
        if (filmRepository.findById(type.getId()).isPresent()) {
            throw new MyAppException("400", String.format("Фильм с id %d уже есть в базе.", type.getId()),
                    HttpStatus.BAD_REQUEST);
        }
        return filmRepository.create(type);
    }

    @Override
    public Film update(Film type) {
        checkFilmById(type.getId());
        return filmRepository.update(type);
    }

    @Override
    public void delete(Long id) {
        checkFilmById(id);
        filmRepository.delete(id);
    }

    @Override
    public void addLike(Long id, Long userId) {
        checkPathVarsForNull(id, userId);
        checkFilmById(id);
        checkUserById(userId);
        filmRepository.addLike(id, userId);
        log.info("Пользователь с id {} поставил лайк фильму с id {}.", userId, id);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        checkPathVarsForNull(id, userId);
        checkFilmById(id);
        checkUserById(userId);
        filmRepository.deleteLike(id, userId);
        log.info("Пользователь с id {} удалил лайк фильму с id {}.", userId, id);
    }

    @Override
    public List<Film> findFirstCountFilms(Integer count) {
        return filmRepository.findFirstCountFilms(count);
    }

    private void checkFilmById(Long... ids) {
        for (Long id : ids) {
            filmRepository.findById(id).orElseThrow(() ->
                    new MyAppException("404", String.format("Фильм с id %d не найден.", id),
                            HttpStatus.NOT_FOUND));
        }
    }

    private void checkUserById(Long... ids) {
        for (Long id : ids) {
            userService.findById(id);
        }
    }

    private void checkPathVarsForNull(Long... pathVars) {
        boolean res = Arrays.stream(pathVars).allMatch(Objects::nonNull);
        if (!res) {
            throw new MyAppException("400", String.format("Переменные пути не могут быть пустыми."),
                    HttpStatus.BAD_REQUEST);
        }
    }
}