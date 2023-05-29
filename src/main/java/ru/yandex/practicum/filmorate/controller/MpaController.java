package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.enumeration.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService mpaService;

    public MpaController(@Qualifier("jdbcMpaService") MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping
    public Collection<Mpa> findAll() {
        return mpaService.findAll();
    }

    @GetMapping("/{id}")
    public Mpa findById(@PathVariable("id") Long id) {
        return mpaService.findById(id);
    }

    @PostMapping
    public Mpa create(@RequestBody Mpa mpa) {
        return mpaService.create(mpa);
    }

    @PutMapping
    public Mpa update(@RequestBody Mpa mpa) {
        return mpaService.update(mpa);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        mpaService.delete(id);
    }
}