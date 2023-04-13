package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        log.info("Зпрос на получение всех фильмов из базы.");
        return films.entrySet().stream()
                .map(k -> k.getValue())
                .collect(Collectors.toList());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            log.info("Фильм с id {} уже есть в базе.", film.getId());
            return film;
        }

        films.put(film.getId(), film);
        log.info("Фильм с id {} был добавлен в базу.", film.getId());
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Фильма с id {} еще нет в базе.", film.getId());
            throw new ValidationException(String.format("Фильм c id %d еще не добавлен в базу.", film.getId()));
        }
        films.put(film.getId(), film);
        log.info("Фильм с id {} был изменен.", film.getId());
        return film;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleException(MethodArgumentNotValidException e) {
        throw new ValidationException(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleException(ValidationException e) {
        throw new ValidationException(e.getMessage());
    }
}
