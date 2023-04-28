package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.InMemoryFilmRepository;
import ru.yandex.practicum.filmorate.repository.impl.InMemoryUserRepository;

import java.time.Duration;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private InMemoryFilmRepository filmRepository;
    @Autowired
    private InMemoryUserRepository userRepository;

    private Film film;
    private User user;

    @BeforeEach
    private void init() {
        film = Film.builder()
                .id(1L)
                .name("Dune: Part One")
                .description("Наследник знаменитого дома Атрейдесов Пол отправляется " +
                        "вместе с семьей на одну из самых опасных планет во Вселенной — Арракис.")
                .releaseDate(LocalDate.of(2021, 9, 16))
                .duration(Duration.ofMinutes(155))
                .build();
    }

    @AfterEach
    private void clearDB() {
        filmRepository.clearDB();
        userRepository.clearDB();
    }

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
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void shouldGetStatus200ForGETWhenFilmsSizeIs2() throws Exception {
        Film mk = Film.builder()
                .id(1L)
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

    @Test
    public void shouldGetStatus200ForPUTWhenFilmAndUserAreCorrect() throws Exception {
        createDB(film);
        createUser(2L);
        mockMvc.perform(put("/films/{id}/like/{userId}", 1, 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetStatus404ForPUTWhenFilmNotCorrect() throws Exception {
        createDB(film);
        mockMvc.perform(put("/films/{id}/like/{userId}", 2, 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetStatus200AndSize1And0WhenAddedFilmAndUser() throws Exception {
        createDB(film);
        createUser(2L);

        mockMvc.perform(put("/films/{id}/like/{userId}", 1, 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/films/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.likes.size()").value(1))
                .andExpect(jsonPath("$.likes[0]").value(2));

        mockMvc.perform(delete("/films/{id}/like/{userId}", 1, 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/films/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.likes.size()").value(0));
    }

    @Test
    public void shouldGetStatus200AndSize1ForPOSTWhenPassedParamWas1() throws Exception {
        createDB(film);
        createUser(2L);

        mockMvc.perform(put("/films/{id}/like/{userId}", 1, 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/films/popular")
                        .param("count", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    private void createDB(Film film) {
        filmRepository.create(film);
    }

    private void createUser(Long id) {
        user = User.builder()
                .id(id)
                .email("test@yandex.ru")
                .login("loginTest")
                .name("nameTest")
                .birthday(LocalDate.of(1990, 02, 07))
                .build();

        userRepository.create(user);
    }
}