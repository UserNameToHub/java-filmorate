package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.MyAppException;
import ru.yandex.practicum.filmorate.mapper.MpaRatingMapper;
import ru.yandex.practicum.filmorate.model.enumeration.Mpa;

import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.*;

@Slf4j
@Repository("jdbcMpaRepository")
@RequiredArgsConstructor
public class JdbcMpaRepository implements MpaRepository {
    private final JdbcTemplate jdbcTemplate;

    private final MpaRatingMapper ratingMapper;

    @Override
    public Collection<Mpa> findAll() {
        String sql = "select * from rating";
        return jdbcTemplate.query(sql, (ratingMapper::mapRow));
    }

    @Override
    public Optional<Mpa> findById(Long id) {
        String sqlSelectAll = "select * from rating " +
                "where id = ?";
        List<Mpa> results = jdbcTemplate.query(sqlSelectAll, ratingMapper::mapRow, id);
        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Mpa create(Mpa type) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("rating")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", type.getName());

        int createdId = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
        return Mpa.forValues(createdId);
    }

    @Override
    public Mpa update(Mpa type) {
        existsById(type.getId());
        String sqlUpdate = "update rating set name = ? " +
                "where id = ?";
        jdbcTemplate.update(sqlUpdate, type.getName());
        log.info("Рейтинг с id-{} был обновлен.", type.getId());
        return type;
    }

    @Override
    public void delete(Long id) {
        existsById(id);
        String sqlDelete = "delete from rating where id = ?";
        jdbcTemplate.update(sqlDelete, id);
    }

    private void existsById(long id) {
        String sql = "select id from rating where id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);
        if (!filmRows.next()) {
            log.info("Жанр с id {} не найден.");
            throw new MyAppException("404", String.format("Рейтинг с id {} не найден.", id), HttpStatus.NOT_FOUND);
        }
    }
}
