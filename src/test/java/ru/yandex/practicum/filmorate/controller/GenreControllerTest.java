package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.yandex.practicum.filmorate.AbstractControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GenreControllerTest extends AbstractControllerTest {
    @Test
    public void shouldGetStatus200ForGetTWhenRequestBodyIsCorrect() throws Exception {
        mockMvc.perform(get("/genres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetCorrectIdAndValueForGetWhenPathVariableIsCorrect() throws Exception {
        mockMvc.perform(get("/genres/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Комедия"))
                .andDo(print());
    }

    @Test
    public void shouldGetStatus404ForGetWhenPathVariableIsNotCorrect() throws Exception {
        mockMvc.perform(get("/genres/{id}", -2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void shouldGetStatus200ForDeleteWhenPathVariableIsCorrect() throws Exception {
        mockMvc.perform(delete("/genres/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void shouldGetStatus404ForDeleteWhenPathVariableIsNotCorrect() throws Exception {
        mockMvc.perform(delete("/genres/{id}", -2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}