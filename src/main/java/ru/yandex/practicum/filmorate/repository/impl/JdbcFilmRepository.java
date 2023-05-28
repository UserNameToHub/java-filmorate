package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
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
        return results.size() == 0? Optional.empty(): Optional.of(results.get(0));
    }

    @Override
    @Transactional
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

        return type;
    }

    @Override
    public Film update(Film type) {
        existsById(type.getId());
        String sqlUpdateFilm = "update films set name = ? , description = ?, release_date = ?, duration = ?, mpa = ? " +
                "where id = ?";
        jdbcTemplate.update(sqlUpdateFilm, type.getName(), type.getDescription(), type.getReleaseDate(),
                type.getDuration().toMinutes(), type.getMpa().getId(), type.getId());
        return type;
    }

    @Override
    public void delete(Long id) {
        existsById(id);
        String sql = "delete from films where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void addLike(Long idFilm, Long userId) {
        existsById(idFilm);
        String sql = "insert into film_likes(film_id, user_id)" +
                "values(?, ?)";
        jdbcTemplate.update(sql, idFilm, userId);
    }

    @Override
    public void deleteLike(Long idFilm, Long userId) {
        existsById(idFilm);
        String sql = "delete from film_likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sql, idFilm, userId);
    }

    @Override
    public List<Film> findFirstCountFilms(Integer count) {
        String sql = "select f.name, COUNT(fl.user_id) as count_likes " +
                "from films as f inner join film_likes as fl on f.ID = fl.film_id " +
                "group by film_id " +
                "order by count_likes desc " +
                "limit ?";
        return jdbcTemplate.query(sql, filmMapper::mapRow, count);
    }

    private void existsById(long id) {
        String sql = "select id from films where id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);
        if (!filmRows.next()) {
            log.info("Фильма с id {} не найден.");
            throw new MyAppException("404", String.format("Фильм с id d% не найден." ,id), HttpStatus.NOT_FOUND);
        }
    }
}
