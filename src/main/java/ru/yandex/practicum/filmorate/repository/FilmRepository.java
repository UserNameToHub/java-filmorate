package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmRepository extends CrudRepository<Film> {
    void addLike(Long idFilm, Long userId);

    void deleteLike(Long idFilm, Long userId);

    List<Film> findFirstCountFilms(Integer count);

    boolean existsById(Long id);
}
