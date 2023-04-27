package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.impl.FilmServiceImpl;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmServiceImpl filmService;

    @Autowired
    public FilmController(FilmServiceImpl filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Запрос на получение списка всех фильмов из базы.");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable("id") long id) {
        log.info("Запрос на получение фильма c id {}.", id);
        return filmService.findById(id);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Запрос на создание фильма.");
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Запрос на обновление фильма.");
        return filmService.update(film);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.info("Запрос на удаление фильма.");
        filmService.delete(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("Запрос на добавление лайка.");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        log.info("Запрос на удаление лайка.");
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getFirstCountFilmsByNumberOfLikes(@RequestParam(value = "count", defaultValue = "10") Integer count) {
        log.info("Запрос на получение списока из первых {} фильмов по количеству лайков.", count);
        return filmService.findFirstCountFilms(count);
    }
}