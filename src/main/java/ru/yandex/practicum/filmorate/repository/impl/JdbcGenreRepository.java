package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.model.enumeration.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.*;

@Slf4j
@Repository("jdbcGenreRepository")
@RequiredArgsConstructor
public class JdbcGenreRepository implements GenreRepository {
    private final JdbcTemplate jdbcTemplate;

    private final GenreMapper genreMapper;

    @Override
    public Collection<Genre> findAll() {
        String sql = "select * from genres";
        return jdbcTemplate.query(sql, (genreMapper::mapRow));
    }

    @Override
    public Optional<Genre> findById(Long id) {
        String sqlSelectAll = "select * from genres " +
                "where id = ?";
        List<Genre> results = jdbcTemplate.query(sqlSelectAll, genreMapper::mapRow, id);
        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Genre create(Genre type) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("genres")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", type.getName());

        int createdId = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
        return Genre.forValues(createdId);
    }

    @Override
    public Genre update(Genre type) {
        String sqlUpdate = "update genres set name = ? " +
                "where id = ?";
        jdbcTemplate.update(sqlUpdate, type.getName());
        return type;
    }

    @Override
    public void delete(Long id) {
        String sqlDelete = "delete from genres where id = ?";
        jdbcTemplate.update(sqlDelete, id);
    }

    @Override
    public boolean existsById(long id) {
        String existsQuery = "select exists(select 1 from genres where id=?)";
        return jdbcTemplate.queryForObject(existsQuery, Boolean.class, id);
    }
}
