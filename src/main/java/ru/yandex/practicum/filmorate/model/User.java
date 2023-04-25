package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validation.annotation.SpaceConstraint;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User {
    @NotNull(message = "Id не может быть пустым.")
    private Integer id;

    @NotBlank
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Неверный формат электронной почты.")
    private String email;

    @NotBlank(message = "логин не может быть пустым.")
    @NotNull(message = "логин не может быть пустым.")
    @SpaceConstraint(message = "логин не может содержать пробелы.")
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;
}
