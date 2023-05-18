package ru.yandex.practicum.filmorate.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class FilmDbRepository implements FilmRepository {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Film> findAll() {
        String sql = "select * from films";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeUser(rs)));
    }

    @Override
    public Optional<Film> findById(Long id) {
        String sql = "select * from films where id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);
        if (filmRows.next()) {
            Film film = Film.builder()
                    .id(filmRows.getLong("id"))
                    .name(filmRows.getString("name"))
                    .description(filmRows.getString("description"))
                    .releaseDate(filmRows.getDate("release_date").toLocalDate())
                    .duration(Duration.ofMinutes(filmRows.getLong("duration")))
                    .rating(convertRatingFromDB(filmRows.getLong("rating_id")))
                    .build();
            log.info("Найден фильм: {}, {}.", film.getId(), film.getName());
            return Optional.of(film);
        } else {
            log.info("Фильма с id {} еще нет в базе.", id);
            return Optional.empty();
        }
    }

    @Override
    public Film create(Film type) {
        if (existsById(type.getId())) {
            log.info("Фильма с id {} уже есть в базе.");
            return type;
        }
        String sql = "update films set id = ?, name = ? , description = ?, release_date = ?, duration = ?, rating_id = ?";
        jdbcTemplate.update(sql, type.getId(), type.getName(), type.getDescription(), type.getReleaseDate(),
                type.getDuration().toMinutes(), convertRatingFromObject(type.getRating()));
        return type;
    }

    @Override
    public Film update(Film type) {
        if (!existsById(type.getId())) {
            log.info("Фильма с id {} еще нет в базе.");
            return type;
        }
        String sql = "update films set id = ?, name = ? , description = ?, release_date = ?, duration = ?, rating_id = ?";
        jdbcTemplate.update(sql, type.getId(), type.getName(), type.getDescription(), type.getReleaseDate(),
                type.getDuration().toMinutes(), convertRatingFromObject(type.getRating()));
        return type;
    }

    @Override
    public void delete(Long id) {
        if (!existsById(id)) {
            log.info("Фильма с id {} еще нет в базе.");
            return;
        }
        String sql = "delete from films where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void addLike(Long idFilm, Long userId) {
        if (!existsById(idFilm) || !existsById(userId)) {
            log.info("Фильма с id {} или пользовател с id {} еще нет в базе.");
            return;
        }
        String sql = "insert into film_likes(film_id, user_id)" +
                "values(?, ?)";
        jdbcTemplate.update(sql, idFilm, userId);
    }

    @Override
    public void deleteLike(Long idFilm, Long userId) {
        if (!existsById(idFilm) || !existsById(userId)) {
            log.info("Фильма с id {} или пользовател с id {} еще нет в базе.");
            return;
        }
        String sql = "delete from film_likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sql, idFilm, userId);
        log.info("Лайк пользователя с id {} был удален у фильма с id {}", idFilm, userId);
    }

    @Override
    public List<Film> findFirstCountFilms(Integer count) {
        String sql = "select f.name, COUNT(user_id) as count_likes " +
                "from films as f inner join film_likes as fl on f.ID = fl.film_id " +
                "group by film_id " +
                "order by count_likes desc " +
                "limit ?";
        return jdbcTemplate.query(sql, ((rs, rowNum) -> makeUser(rs)), count);
    }

    public boolean existsById(Long id) {
        String sql = "select * from films as o where o.id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        return userRows.next();
    }

    private Film makeUser(ResultSet rs) throws SQLException {
        Film film = Film.builder().id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(Duration.ofMinutes(rs.getLong("duration")))
                .rating(convertRatingFromDB(rs.getLong("rating_id")))
                .build();
        return film;
    }

    private String convertRatingFromDB(long ratingLong) {
        String sql = "select rating_mpa from rating where id = ?";
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet(sql, ratingLong);
        return ratingRows.next() ? ratingRows.getString("rating_map") : "unknown";
    }

    private Long convertRatingFromObject(String ratingString) {
        String sql = "select id from rating where rating_mpa = ?";
        SqlRowSet ratingRows = jdbcTemplate.queryForRowSet(sql, ratingString);
        return ratingRows.next() ? ratingRows.getLong("id") : null;
    }
}