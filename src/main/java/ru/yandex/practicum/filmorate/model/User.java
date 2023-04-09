package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import ru.yandex.practicum.filmorate.deserializer.CustomLocalDateDeserializer;
import ru.yandex.practicum.filmorate.validate.annotation.SpaceConstraint;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter @Setter
@EqualsAndHashCode
@ToString
//@Builder
public class User {

    @EqualsAndHashCode.Exclude
    private final int id;

    @NotBlank
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Неверный формат электронной почты")
    private String email;

    @NotBlank(message = "логин не может быть пустым")
    @SpaceConstraint(message = "логин не может содержать пробелы")
    private String login;

    private String name;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name.isBlank()? login: name;
        this.birthday = birthday;
        this.id = this.hashCode();
    }
}
