package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Component
public class LikeMapper implements RowMapper<Long> {
    @Override
    public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("user_id");
    }

    public void reverseMapRow(Set<Long> likes, Long id, JdbcTemplate jdbcTemplate) {
        String sqlAllLikes = "insert into film_likes(film_id, user_id) " +
                "values(? ,?)";
        if (!likes.isEmpty()) {
            likes.stream().forEach(x -> {
                jdbcTemplate.update(sqlAllLikes, id, x.longValue());
            });
        }
    }
}
