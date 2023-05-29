package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.enumeration.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    public GenreController(@Qualifier("jdbcGenreService") GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public Collection<Genre> findAll() {
        return genreService.findAll();
    }

    @GetMapping("/{id}")
    public Genre findById(@PathVariable("id") Long id) {
        return genreService.findById(id);
    }

    @PostMapping
    public Genre create(@RequestBody Genre genre) {
        return genreService.create(genre);
    }

    @PutMapping
    public Genre update(@RequestBody Genre genre) {
        return genreService.update(genre);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        genreService.delete(id);
    }
}