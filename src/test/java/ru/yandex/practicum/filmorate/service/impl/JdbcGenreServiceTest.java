package ru.yandex.practicum.filmorate.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.AbstractServiceTest;
import ru.yandex.practicum.filmorate.model.enumeration.Genre;
import ru.yandex.practicum.filmorate.repository.impl.JdbcGenreRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcGenreServiceTest extends AbstractServiceTest {
    private final JdbcGenreRepository genreRepository;

    @Test
    public void testFindByIdParam() {
        Optional<Genre> genreOptional = genreRepository.findById(3L);
        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(Genre ->
                        assertThat(Genre).hasFieldOrPropertyWithValue("id", 3)
                                .hasFieldOrPropertyWithValue("name", "Мультфильм"));
    }

    @Test
    public void testFindAll() {
        int size = genreRepository.findAll().size();
        assertThat(size).isEqualTo(6);
    }

    @Test
    public void testDelete() throws JsonProcessingException {
        Optional<Genre> optionalGenre = genreRepository.findById(1L);
        assertThat(optionalGenre).isNotEmpty();

        genreRepository.delete(1L);

        Optional<Genre> optionalDeletedGenre = genreRepository.findById(1L);
        assertThat(optionalDeletedGenre).isEmpty();
    }
}