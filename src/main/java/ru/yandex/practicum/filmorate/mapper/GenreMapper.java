package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.enumeration.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Component
public class GenreMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Genre.forValues(rs.getInt("id"));
    }

    public void reverseMapRow(Set<Genre> genres, Long id, JdbcTemplate jdbcTemplate) {
        String sqlDelete = "delete from film_genres where film_id = ?";
        String sqlInsert = "insert into film_genres(film_id, genre_id) " +
                "values(?, ?)";
        jdbcTemplate.update(sqlDelete, id);
        if (!genres.isEmpty()) {
            genres.stream().forEach(e -> {
                jdbcTemplate.update(sqlInsert, id, e.getId());
            });
        }
    }
}
