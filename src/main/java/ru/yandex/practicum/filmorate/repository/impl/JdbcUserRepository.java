package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.MyAppException;
import ru.yandex.practicum.filmorate.mapper.FriendMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;

@Slf4j
@Repository("jdbcUserRepository")
@RequiredArgsConstructor
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    private final UserMapper userMapper;

    private final FriendMapper friendMapper;

    @Override
    public Collection<User> findAll() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, (userMapper::mapRow));
    }

    @Override
    public Optional<User> findById(Long id) {
        String sqlSelectAll = "select * from users " +
                "where id = ?";
        List<User> results = jdbcTemplate.query(sqlSelectAll, userMapper::mapRow, id);
        return results.size() == 0 ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public User create(User type) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("email", type.getEmail());
        parameters.put("login", type.getLogin());
        parameters.put("name", type.getName());
        parameters.put("birthday", type.getBirthday());

        long createdId = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        type.setId(createdId);

        friendMapper.reverseMapRow(type.getFriends(), createdId, jdbcTemplate);

        return type;
    }

    @Override
    public User update(User type) {
        existsById(type.getId());
        String sql = "update users set email = ? , login = ?, name = ?, birthday = ? " +
                "where id = ?";
        jdbcTemplate.update(sql, type.getEmail(), type.getLogin(), type.getName(), type.getBirthday(), type.getId());
        log.info("Информация о пользователе с id-{} была обновлена.", type.getId());
        return type;
    }

    @Override
    public void delete(Long id) {
        existsById(id);

        String sqlDelete = "delete from users where id = ?";
        jdbcTemplate.update(sqlDelete, id);
        log.info("Пользователь с id {} был удален из базы.");
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        existsById(id);
        existsById(friendId);
        String sql = "insert into user_friends(user_id, friend_id) " +
                "values (?, ?)";
        jdbcTemplate.update(sql, id, friendId);
        log.info("Друзья c id {} и id {} были добавлены.");
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        existsById(id);
        existsById(friendId);
        String sql = "delete from user_friends where user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, id, friendId);
        jdbcTemplate.update(sql, friendId, id);
        log.info("Друзья c id {} и id {} были добавлены.");
    }

    @Override
    public List<User> findAllFriends(Long id) {
        String sql = "select * " +
                "from users " +
                "where id in(" +
                "select friend_id " +
                "from user_friends " +
                "where user_id = ?)";
        return jdbcTemplate.query(sql, userMapper::mapRow, id);
    }

    @Override
    public List<User> findCommonFriends(Long id, Long otherId) {
        String sql = "select * from users as u " +
                "where u.id in (" +
                "select friend_id " +
                "from user_friends as uf " +
                "where (uf.user_id = ? or uf.user_id = ?) " +
                "group by uf.friend_id " +
                "having count(uf.friend_id) > 1)";
        return jdbcTemplate.query(sql, userMapper::mapRow, id, otherId);
    }

    public void existsById(long id) {
        String sql = "select id from users where id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);
        if (!filmRows.next()) {
            log.info("Пользователь с id {} не найден.");
            throw new MyAppException("404", String.format("Пользователь с id {} не найден.", id), HttpStatus.NOT_FOUND);
        }
    }
}
