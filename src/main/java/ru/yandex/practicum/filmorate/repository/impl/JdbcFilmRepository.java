package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.MyAppException;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.LikeMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.*;

@Slf4j
@Repository("jdbcFilmRepository")
@RequiredArgsConstructor
public class JdbcFilmRepository implements FilmRepository {
    private final JdbcTemplate jdbcTemplate;

    private final FilmMapper filmMapper;

    private final GenreMapper categoryMapper;

    private final LikeMapper likeMapper;

    private final JdbcUserRepository userRepository;

    @Override
    public Collection<Film> findAll() {
        String sql = "select * from films";
        return jdbcTemplate.query(sql, filmMapper::mapRow);
    }

    @Override
    public Optional<Film> findById(Long id) {
        String sql = "select * " +
                "from films " +
                "where id = ?";
        List<Film> results = jdbcTemplate.query(sql, filmMapper::mapRow, id);
        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Film create(Film type) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", type.getName());
        parameters.put("description", type.getDescription());
        parameters.put("release_date", type.getReleaseDate());
        parameters.put("duration", type.getDuration().toMinutes());
        parameters.put("mpa", type.getMpa().getId());

        long createdId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        type.setId(createdId);

        categoryMapper.reverseMapRow(type.getGenres(), createdId, jdbcTemplate);
        likeMapper.reverseMapRow(type.getLikes(), createdId, jdbcTemplate);

        log.info("фильм с id - {} создан.", createdId);
        return type;
    }

    @Override
    public Film update(Film type) {
        existsById(type.getId());
        String sqlUpdateFilm = "update films set name = ? , description = ?, release_date = ?, duration = ?, mpa = ? " +
                "where id = ?";
        jdbcTemplate.update(sqlUpdateFilm, type.getName(), type.getDescription(), type.getReleaseDate(),
                type.getDuration().toMinutes(), type.getMpa().getId(), type.getId());
        categoryMapper.reverseMapRow(type.getGenres(), type.getId(), jdbcTemplate);

        log.info("фильм с id - {} обновлен.", type.getId());
        return type;
    }

    @Override
    public void delete(Long id) {
        existsById(id);
        String sql = "delete from films where id = ?";
        jdbcTemplate.update(sql, id);
        log.info("фильм с id - {} удален.", id);
    }

    @Override
    public void addLike(Long idFilm, Long userId) {
        existsById(idFilm);
        userRepository.existsById(userId);
        String sql = "insert into film_likes(film_id, user_id)" +
                "values(?, ?)";
        jdbcTemplate.update(sql, idFilm, userId);
        log.info("Лайк от пользователя с id-{}  добавлен фильму с id-{}.", userId, idFilm);
    }

    @Override
    public void deleteLike(Long idFilm, Long userId) {
        existsById(idFilm);
        userRepository.existsById(userId);
        String sql = "delete from film_likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sql, idFilm, userId);
        log.info("Пользователя с id-{}  удалил лайк фильму с id-{}.", userId, idFilm);
    }

    @Override
    public List<Film> findFirstCountFilms(Integer count) {
        String sqlSelectFirst = "select f.* " +
                "from films as f left join FILM_GENRES FG on f.id = fg.film_id " +
                "group by f.id " +
                "order by count(fg.genre_id) desc " +
                "limit ?";
        return jdbcTemplate.query(sqlSelectFirst, filmMapper::mapRow, count);
    }

    private void existsById(long id) {
        String sql = "select id from films where id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);
        if (!filmRows.next()) {
            log.info("Фильма с id {} не найден.");
            throw new MyAppException("404", String.format("Фильм с id {} не найден.", id), HttpStatus.NOT_FOUND);
        }
    }
}
