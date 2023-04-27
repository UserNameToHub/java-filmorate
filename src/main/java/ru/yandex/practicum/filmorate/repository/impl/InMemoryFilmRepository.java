package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryFilmRepository implements FilmRepository {
    private static final Map<Long, Film> IN_MEMORY_DB = new HashMap<>();

    @Override
    public Collection<Film> findAll() {
        return IN_MEMORY_DB.values();
    }

    @Override
    public Optional<Film> findById(Long id) {
        return IN_MEMORY_DB.entrySet().stream()
                .filter(k -> k.getKey() == id)
                .map(k -> k.getValue())
                .findFirst();
    }

    @Override
    public Film create(Film type) {
        IN_MEMORY_DB.put(type.getId(), type);
        return type;
    }

    @Override
    public Film update(Film type) {
        IN_MEMORY_DB.put(type.getId(), type);
        return type;
    }

    @Override
    public void delete(Long id) {
        IN_MEMORY_DB.remove(id);
    }

    @Override
    public void addLike(Long idFilm, Long userId) {
        IN_MEMORY_DB.get(idFilm).getLikes().add(userId);
    }

    @Override
    public void deleteLike(Long idFilm, Long userId) {
        IN_MEMORY_DB.get(idFilm).getLikes().remove(userId);
    }

    @Override
    public List<Film> findFirstCountFilms(Integer count) {
        return IN_MEMORY_DB.values().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
