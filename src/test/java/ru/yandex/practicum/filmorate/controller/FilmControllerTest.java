package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Film film;

    @BeforeEach
    private void init() {
        film = Film.builder()
                .id(1)
                .name("Dune: Part One")
                .description("Наследник знаменитого дома Атрейдесов Пол отправляется " +
                        "вместе с семьей на одну из самых опасных планет во Вселенной — Арракис.")
                .releaseDate(LocalDate.of(2021, 9, 16))
                .duration(Duration.ofMinutes(155))
                .build();
    }

    // POST
    @Test
    public void shouldGetStatus200ForPOSTWhenRequestBodyIsCorrect() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk());
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
    public void shouldGetStatus400ForPOSTWhenFilmDurationIs155() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.duration").value(155));
    }

    @Test
    public void shouldGetStatus400ForPOSTWhenRequestBodyIsEmpty() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    // GET
    @Test
    public void shouldGetStatus200ForGETWhenFilmsSizeIs2() throws Exception {
        Film mk = Film.builder()
                .id(1)
                .name("Mortal Kombat")
                .description("Боец смешанных единоборств Коул Янг не " +
                        "раз соглашался проиграть за деньги. ")
                .releaseDate(LocalDate.of(2021, 05, 07))
                .duration(Duration.ofMinutes(110))
                .build();

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(film)))
                .andExpect(status().isOk());
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mk)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andDo(print());
    }
}