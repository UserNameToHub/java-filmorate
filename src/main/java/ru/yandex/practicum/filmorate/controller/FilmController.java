package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;

@RestController
public class FilmController {

    private final HashMap<Long, Film> films = new HashMap<>();

    @GetMapping("/films")
    public HashMap<Long, Film> findAll() {
        return films;
    }

    @PostMapping("/film")
    public Film create(@RequestBody Film film) {
        films.put(film.id, film);
        return film;
    }

    @PutMapping("/film")
    public Film update(@RequestBody Film film) {
        films.put(film.id, film);
        return film;
    }
}
