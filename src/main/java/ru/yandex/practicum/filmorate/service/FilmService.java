package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService extends CrudService<Film> {

    void addLike(Long id, Long userId);

    void deleteLike(Long id, Long userId);

    List<Film> findFirstCountFilms(Integer count);

}
