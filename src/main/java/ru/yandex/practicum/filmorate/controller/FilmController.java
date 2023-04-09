package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RestController
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping("/films")
    public HashMap<Integer, Film> findAll() {
        Film mk = new Film("Mortal combat", "action",
                LocalDate.of(1987, 02, 07), Duration.ofMinutes(-1));
        films.put(mk.getId(), mk);
        return films;
    }

    @PostMapping("/film")
    public String create(@Valid @RequestBody Film film, Errors errors) {
        films.put(film.getId(), film);
        return "error";
    }

    @PutMapping("/film")
    public Film update(@Valid @RequestBody Film film) {
        films.put(film.getId(), film);
        return film;
    }
}
