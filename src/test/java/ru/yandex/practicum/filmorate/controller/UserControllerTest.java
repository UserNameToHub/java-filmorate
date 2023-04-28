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
import ru.yandex.practicum.filmorate.exception.MyAppException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.InMemoryUserRepository;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private InMemoryUserRepository repository;

    private User user;
    private User user2;
    private User user3;

    @BeforeEach
    private void initUser() {
        user = User.builder()
                .id(1l)
                .email("test@yandex.ru")
                .login("loginTest")
                .name("nameTest")
                .birthday(LocalDate.of(1990, 02, 07))
                .build();

        user2 = User.builder()
                .id(2l)
                .email("test2@yandex.ru")
                .login("loginTest2")
                .name("nameTest2")
                .birthday(LocalDate.of(1993, 02, 07))
                .build();

        user3 = User.builder()
                .id(3l)
                .email("test3@yandex.ru")
                .login("loginTest3")
                .name("nameTest3")
                .birthday(LocalDate.of(1996, 03, 06))
                .build();
    }

    @AfterEach
    private void clearDB() {
        repository.clearDB();
    }

    @Test
    public void shouldGetStatus200ForPOSTWhenRequestBodyIsCorrect() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetStatus400ForPOSTWhenEmailHasNoDog() throws Exception {
        user.setEmail("testyandex.ru");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetStatus400ForPOSTWhenEmailHasCyrillic() throws Exception {
        user.setEmail("тест@yandex.ru");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetStatus400ForPOSTWhenLoginHasSpace() throws Exception {
        user.setLogin("test login");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetStatus400ForPOSTWhenLoginIsNull() throws Exception {
        user.setLogin("");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldGetStatus400ForPOSTWhenBirthdayIsFuture() throws Exception {
        user.setBirthday(LocalDate.of(2111, 02, 07));
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    public void shouldGetStatus400ForPOSTWhenRequestBodyIsEmpty() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void shouldGetNameLikeLoginForPOSTWhenNameIsEmpty() throws Exception {
        user.setName("");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getLogin()))
                .andDo(print());
    }

    @Test
    public void shouldGetSize1IsForGETWhenListHasUser() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andDo(print());
    }

    @Test
    public void shouldGet200ForDELETEWhenIdIsTrue() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)));

        mockMvc.perform(delete("/users/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGet400AndThrowMyAppExceptionForDELETEWhenIdIsFalse() throws Exception {
        mockMvc.perform(delete("/users/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(result -> result.getResolvedException().getClass().equals(MyAppException.class));
    }

    @Test
    public void shouldGet200AndAddedFriendsForGETWhenDBHasTrueUsers() throws Exception {
        addUserInDB(user);
        addUserInDB(user2);

        mockMvc.perform(put("/users/1/friends/2"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/{id}/friends", 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        mockMvc.perform(get("/users/{id}/friends", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2));
    }

    @Test
    public void shouldGet200AndCommonFriendsForGETWhenUsersHaveCommonFriends() throws Exception {
        addUserInDB(user);
        addUserInDB(user2);
        addUserInDB(user3);

        mockMvc.perform(put("/users/1/friends/3"))
                .andExpect(status().isOk());
        mockMvc.perform(put("/users/2/friends/3"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/{id}/friends/common/{otherId}", 1, 2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3));

        mockMvc.perform(get("/users/{id}/friends/common/{otherId}", 2, 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3));
    }

    private void addUserInDB(User user) {
        repository.create(user);
    }
}