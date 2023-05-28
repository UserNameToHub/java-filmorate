package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserMapper implements RowMapper<User> {
    private final FriendMapper friendMapper;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = User.builder().id(rs.getLong("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();

        Set<Long> friends = user.getFriends();
        String sqlSelectAllFriend = "select * from user_friends " +
                "where user_id = ?";
        friends.addAll(jdbcTemplate.query(sqlSelectAllFriend, friendMapper::mapRow, user.getId()));

        return user;
    }
}
