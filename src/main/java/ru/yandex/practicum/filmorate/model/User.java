package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validation.annotation.SpaceConstraint;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {
    @EqualsAndHashCode.Exclude
    private final int id;

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

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = Objects.isNull(name) || name.isBlank()? login: name;
        this.birthday = birthday;
        this.id = this.hashCode();
    }
}
