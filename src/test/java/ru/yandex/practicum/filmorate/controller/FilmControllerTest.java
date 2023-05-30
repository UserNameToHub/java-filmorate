package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.AbstractControllerTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FilmControllerTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Film film;

    @BeforeEach
    private void init() {
        film = Film.builder()
                .name("Dune: Part One")
                .description("Наследник знаменитого дома Атрейдесов Пол отправляется " +
                        "вместе с семьей на одну из самых опасных планет во Вселенной — Арракис.")
                .releaseDate(LocalDate.of(2021, 9, 16))
                .duration(Duration.ofMinutes(155))
                .build();


    }

    @Test
    public void shouldGetStatus400ForPOSTWhenFilmNameIsEmpty() throws Exception {
        film.setName("");
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetStatus400ForPOSTWhenFilmNameIsNull() throws Exception {
        film.setName(null);
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetStatus400ForPOSTWhenFilmDescriptionIs201chars() throws Exception {
        film.setDescription("Наследник знаменитого дома Атрейдесов Пол отправляется вместе с семьей на одну из самых " +
                "опасных планет во Вселенной — Арракис. Здесь нет ничего, кроме песка, палящего солнца, " +
                "гигантских чудовищ и основно");
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetStatus400ForPOSTWhenFilmReleaseDateIs1890() throws Exception {
        film.setReleaseDate(LocalDate.of(1890, 02, 12));
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetStatus400ForPOSTWhenFilmDurationIs0() throws Exception {
        film.setDuration(Duration.ZERO);
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetStatus400ForPOSTWhenFilmDurationIsDownZero() throws Exception {
        film.setDuration(Duration.ofMinutes(-1));
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetStatus400ForPOSTWhenRequestBodyIsEmpty() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void shouldGetStatus200ForGETWhenFilmsSizeIs5() throws Exception {
        mockMvc.perform(get("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(5))
                .andDo(print());
    }

    @Test
    public void shouldGetStatus200ForPUTWhenFilmAndUserAreCorrect() throws Exception {
        mockMvc.perform(put("/films/{id}/like/{userId}", 1, 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetStatus404ForPUTWhenFilmNotCorrect() throws Exception {
        mockMvc.perform(put("/films/{id}/like/{userId}", -2, 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetStatus200AndSize1ForPOSTWhenPassedParamWas1() throws Exception {
        mockMvc.perform(put("/films/{id}/like/{userId}", 1, 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/films/popular")
                        .param("count", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

//    private void createDB(Film film) {
//        filmRepository.create(film);
//    }

//    private void createUser(Long id) {
//        user = User.builder()
//                .id(id)
//                .email("test@yandex.ru")
//                .login("loginTest")
//                .name("nameTest")
//                .birthday(LocalDate.of(1990, 02, 07))
//                .build();
//
//        userRepository.create(user);
//    }
}