package ru.yandex.practicum.filmorate.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.AbstractServiceTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.impl.JdbcFilmRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcFilmServiceTest extends AbstractServiceTest {
    private final JdbcFilmRepository filmRepository;

    private final ObjectMapper objectMapper;

    @ParameterizedTest()
    @ValueSource(longs = {1, 2, 3, 4, 5})
    public void testFindByIdParam(Long args) {
        Optional<Film> filmOptional = filmRepository.findById(args);
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", args));
    }

    @Test
    public void testFindAll() {
        int size = filmRepository.findAll().size();
        assertThat(size).isEqualTo(5);
    }

    @Test
    public void testUpdate() throws JsonProcessingException {
        Film curFilm = filmRepository.findById(1L).get();
        assertThat(curFilm).hasFieldOrPropertyWithValue("name", "Дюна");

        String content = "{\"id\": 1,\"name\":\"newFilm\",\"description\":\"Focus\",\"releaseDate\":\"2021-09-21\"," +
                "\"duration\":112, \"mpa\":{\"id\": 1}}";
        Film updateFilm = objectMapper.readValue(content, Film.class);

        filmRepository.update(updateFilm);
        Film film = filmRepository.findById(1L).get();
        assertThat(film).hasFieldOrPropertyWithValue("name", "newFilm");
    }

    @Test
    public void testDelete() throws JsonProcessingException {
        Optional<Film> optionalFilm = filmRepository.findById(1L);
        assertThat(optionalFilm).isNotEmpty();

        filmRepository.delete(1L);

        Optional<Film> optionalDeletedFilm = filmRepository.findById(1L);
        assertThat(optionalDeletedFilm).isEmpty();
    }

    @Test
    public void testAddLike() throws JsonProcessingException {
        Film beforeFilm = filmRepository.findById(1L).get();
        assertThat(beforeFilm.getLikes().size()).isEqualTo(4);

        filmRepository.addLike(1L, 9L);

        Film afterFilm = filmRepository.findById(1L).get();
        assertThat(afterFilm.getLikes().size()).isEqualTo(5);
    }

    @Test
    public void testDeleteLike() throws JsonProcessingException {
        Film beforeFilm = filmRepository.findById(1L).get();
        assertThat(beforeFilm.getLikes().size()).isEqualTo(4);

        filmRepository.deleteLike(1L, 10L);

        Film afterFilm = filmRepository.findById(1L).get();
        assertThat(afterFilm.getLikes().size()).isEqualTo(3);
    }

    @Test
    public void testFindFirstCountFilms() throws JsonProcessingException {
        int size = filmRepository.findFirstCountFilms(3).size();
        assertThat(size).isEqualTo(3);

        int size2 = filmRepository.findFirstCountFilms(5).size();
        assertThat(size2).isEqualTo(5);

        int size3 = filmRepository.findFirstCountFilms(1).size();
        assertThat(size3).isEqualTo(1);
    }
}