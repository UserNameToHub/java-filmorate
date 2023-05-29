package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final InMemoryFilmRepository filmRepository;

    private final UserServiceImpl userService;

    private Long idF = 1L;

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
        type.setId(idF++);

        if (filmRepository.existsById(type.getId())) {
            throw new MyAppException("400", String.format("Фильм с id %d уже есть в базе.", type.getId()),
                    HttpStatus.BAD_REQUEST);
        }
        return filmRepository.create(type);
    }

    @Override
    public Film update(Film type) {
        log.info("Запрос на обновление фильма.");
        checkFilmById(type.getId());
        return filmRepository.update(type);
    }

    @Override
    public void delete(Long id) {
        log.info("Запрос на удаление фильма.");
        checkFilmById(id);
        filmRepository.delete(id);
    }

    @Override
    public void addLike(Long id, Long userId) {
        log.info("Запрос на добавление лайка.");
        checkPathVarsForNull(id, userId);
        checkFilmById(id);
        checkUserById(userId);
        filmRepository.addLike(id, userId);
        log.info("Пользователь с id {} поставил лайк фильму с id {}.", userId, id);
    }

    @Override
    public void deleteLike(Long id, Long userId) {
        log.info("Запрос на удаление лайка.");
        checkPathVarsForNull(id, userId);
        checkFilmById(id);
        checkUserById(userId);
        filmRepository.deleteLike(id, userId);
        log.info("Пользователь с id {} удалил лайк фильму с id {}.", userId, id);
    }

    @Override
    public List<Film> findFirstCountFilms(Integer count) {
        log.info("Запрос на получение списока из первых {} фильмов по количеству лайков.", count);
        return filmRepository.findFirstCountFilms(count);
    }

    private void checkFilmById(Long... ids) {
        for (Long id : ids) {
            if (!filmRepository.existsById(id)) {
                throw new MyAppException("404", String.format("Фильм с id %d не найден.", id),
                        HttpStatus.NOT_FOUND);
            }
        }
    }

    private void checkUserById(Long... ids) {
            userService.checkUserById(ids);
    }

    private void checkPathVarsForNull(Long... pathVars) {
        boolean res = Arrays.stream(pathVars).allMatch(Objects::nonNull);
        if (!res) {
            throw new MyAppException("400", String.format("Переменные пути не могут быть пустыми."),
                    HttpStatus.BAD_REQUEST);
        }
    }
}