package ru.yandex.practicum.filmorate.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class UserDbRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserDbRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<User> findAll() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeUser(rs)));
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "select o from users as o where o.id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        if (userRows.next()) {
            User user = User.builder().id(userRows.getLong("user_id"))
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("name"))
                    .birthday(userRows.getDate("birthday").toLocalDate()).build();

            log.info("Найден пользователь: {}, {}.", user.getId(), user.getName());
            return Optional.of(user);
        } else {
            log.info("Пользователя с id {} еще нет в базе.", id);
            return Optional.empty();
        }
    }

    @Override
    public User create(User type) {
        String sql = "insert into users(id, email, login, name, birthday) " +
                "values(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, type.getId(), type.getEmail(), type.getLogin(), type.getName(), type.getBirthday());
        return type;
    }

    @Override
    public User update(User type) {
        if (!existsById(type.getId())) {
            log.info("Пользователя с id {} еще нет в базе.");
            return type;
        }
        String sql = "update users set id = ?, email = ? , login = ?, name = ?, birthday = ? " +
                "where id = ?";
        jdbcTemplate.update(sql, type.getId(), type.getEmail(), type.getLogin(), type.getName(), type.getBirthday());
        return type;
    }

    @Override
    public void delete(Long id) {
        if (!existsById(id)) {
            log.info("Пользователя с id {} еще нет в базе.");
        } else {
            String sql = "delete from users where id = ?";
            jdbcTemplate.update(sql, id);
            log.info("Пользователь с id {} был удален из базы.");
        }
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        if (!existsById(id)) {
            log.info("Пользователя с id {} еще нет в базе.");
        } else {
            String sql = "update user_friends set user_id = ?, other_user_id = ?";
            jdbcTemplate.update(sql, id, friendId);
            jdbcTemplate.update(sql, friendId, id);
            log.info("Друзья c id {} и id {} были добавлены.");
        }
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        if (!existsById(id)) {
            log.info("Пользователя с id {} еще нет в базе.");
        } else {
            String sql = "delete from user_friends where user_id = ? AND other_user_id = ?";
            jdbcTemplate.update(sql, id, friendId);
            jdbcTemplate.update(sql, friendId, id);
            log.info("Друзья c id {} и id {} были добавлены.");
        }
    }

    @Override
    public List<User> findAllFriends(Long id) {
        String sql = "select * " +
                "from users as o " +
                "where o.id in(" +
                "select u.other_user_id " +
                "from user_friends as u " +
                "where u.user_id = ?)";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeUser(rs)), id);
    }

    @Override
    public List<User> findCommonFriends(Long id, Long otherId) {
        String sql = "select * from users as u " +
                "where u.id in (" +
                "select other_user_id " +
                "from user_friends as uf " +
                "where (uf.user_id = ? or uf.user_id = ?) and uf.is_confirm = true " +
                "group by uf.other_user_id " +
                "having count(uf.other_user_id) > 1)";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeUser(rs)), id, otherId);
    }

    public boolean existsById(Long id) {
        String sql = "select * from users as o where o.id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        return userRows.next();
    }

    private User makeUser(ResultSet rs) throws SQLException {
        User user = User.builder().id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate()).build();
        return user;
    }
}
