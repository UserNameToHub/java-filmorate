package ru.yandex.practicum.filmorate.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.enumeration.Genre;
import ru.yandex.practicum.filmorate.model.enumeration.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FilmMapper implements RowMapper<Film> {
    private final MpaRatingMapper mpaRatingMapper;
    private final GenreMapper genreMapper;
    private final JdbcTemplate jdbcTemplate;
    private final LikeMapper likeMapper;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(Duration.ofMinutes(rs.getLong("duration")))
                .mpa(Mpa.forValues(rs.getInt("mpa")))
                .build();

        Set<Genre> genres = film.getGenres();
        Set<Long> likes = film.getLikes();

        String sqlAllGenres = "select * from genres " +
                "where id in(" +
                "select genre_id from film_genres " +
                "where film_id = ?" +
                ")";
        genres.addAll(jdbcTemplate.query(sqlAllGenres, genreMapper::mapRow, film.getId()));

        String sqlAllLikes = "select user_id " +
                "from film_likes " +
                "where film_id = ?";
        likes.addAll(jdbcTemplate.query(sqlAllLikes, likeMapper::mapRow, film.getId()));

        return film;
    }
}