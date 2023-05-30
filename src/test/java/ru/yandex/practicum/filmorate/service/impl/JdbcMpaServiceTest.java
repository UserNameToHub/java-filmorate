package ru.yandex.practicum.filmorate.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.AbstractServiceTest;
import ru.yandex.practicum.filmorate.model.enumeration.Mpa;
import ru.yandex.practicum.filmorate.repository.impl.JdbcMpaRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcMpaServiceTest extends AbstractServiceTest {
    private final JdbcMpaRepository mpaRepository;

    @Test
    public void testFindByIdParam() {
        Optional<Mpa> mpaOptional = mpaRepository.findById(1l);
        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(Mpa ->
                        assertThat(Mpa).hasFieldOrPropertyWithValue("id", 1));
    }

    @Test
    public void testFindAll() {
        int size = mpaRepository.findAll().size();
        assertThat(size).isEqualTo(5);
    }

    @Test
    public void testDelete() throws JsonProcessingException {
        Optional<Mpa> optionalFilm = mpaRepository.findById(1L);
        assertThat(optionalFilm).isNotEmpty();

        mpaRepository.delete(1L);

        Optional<Mpa> optionalDeletedFilm = mpaRepository.findById(1L);
        assertThat(optionalDeletedFilm).isEmpty();
    }
}