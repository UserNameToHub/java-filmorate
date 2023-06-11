package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.enumeration.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MpaRatingMapper implements RowMapper<Mpa> {
    @Override
    public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.forValues(rs.getInt("id"));
    }
}
