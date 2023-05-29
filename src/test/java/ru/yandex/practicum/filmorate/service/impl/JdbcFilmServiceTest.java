package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.impl.JdbcFilmRepository;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = {"classpath:db/schema.sql", "classpath:db/dataTest.sql"}, config = @SqlConfig(encoding = "UTF-8"))
class JdbcFilmServiceTest {
    private final JdbcFilmRepository filmRepository;

    @Test
    public void testFindFilmById() {
        Optional<Film> filmOptional = filmRepository.findById(1L);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L));
    }

    @Test
    public void testFindAll() {
        Collection<Film> allFilms = filmRepository.findAll();
        assertThat(allFilms).asList();
    }
}