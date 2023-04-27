package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validation.annotation.SpaceConstraint;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private Long id;

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

    private final Set<Long> friends = new HashSet<>();
}
