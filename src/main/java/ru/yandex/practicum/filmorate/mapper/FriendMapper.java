package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Component
public class FriendMapper implements RowMapper<Long> {
    @Override
    public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("id");
    }

    public void reverseMapRow(Set<Long> likes, Long id, JdbcTemplate jdbcTemplate) {
        String sqlAllLikes = "insert into user_friends(user_id, fiend_id) " +
                "values(? ,?)";
        if (!likes.isEmpty()) {
            likes.stream().forEach(x -> {
                jdbcTemplate.update(sqlAllLikes, id, x.longValue());
            });
        }
    }
}
