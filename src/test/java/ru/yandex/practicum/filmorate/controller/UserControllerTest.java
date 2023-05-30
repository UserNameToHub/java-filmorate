package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.AbstractControllerTest;
import ru.yandex.practicum.filmorate.exception.MyAppException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest extends AbstractControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    private void initUser() {
        user = User.builder()
                .email("test@yandex.ru")
                .login("loginTest")
                .name("nameTest")
                .birthday(LocalDate.of(1990, 02, 07))
                .build();
    }

    @Test
    public void shouldUserForGETWhenIdIsCorrect() throws Exception {
        mockMvc.perform(get("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("user1@yandex.ru"))
                .andExpect(jsonPath("$.login").value("user1"))
                .andExpect(jsonPath("$.name").value("user1"))
                .andExpect(jsonPath("$.birthday").value("1993-03-09"))
                .andDo(print());
    }

    @Test
    public void shouldStatus404ForGETWhenIdIsNotCorrect() throws Exception {
        mockMvc.perform(get("/users/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
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
    public void shouldGet200ForDELETEWhenIdIsTrue() throws Exception {
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
        mockMvc.perform(get("/users/{id}/friends", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(8));

        mockMvc.perform(get("/users/{id}/friends", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].id").value(10));
    }

    @Test
    public void shouldGet200AndCommonFriendsForGETWhenUsersHaveCommonFriends() throws Exception {
        mockMvc.perform(get("/users/{id}/friends/common/{otherId}", 1, 3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(8))
                .andDo(print());

        mockMvc.perform(get("/users/{id}/friends/common/{otherId}", 10, 8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andDo(print());
    }
}