package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.deserializer.CustomDurationDeserializer;
import ru.yandex.practicum.filmorate.validate.annotation.ReleaseDateConstraint;
import ru.yandex.practicum.filmorate.serializer.CustomDurationSerializer;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
//@Builder
public class Film {

    @EqualsAndHashCode.Exclude
    private final int id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Length(max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description;

    @JsonFormat(pattern = "dd.MM.yyyy")
    @ReleaseDateConstraint(value = "28.12.1895", message = "Дата релиза — не раньше 28 декабря 1895 года.")
    private LocalDate releaseDate;

    @JsonDeserialize(using = CustomDurationDeserializer.class)
    @JsonSerialize(using = CustomDurationSerializer.class)
    @DurationMin(nanos = 1, message = "Продолжительность фильма не может быть меньше или равно нулю.")
    private Duration duration;

    public Film(String name, String description, LocalDate releaseDate, Duration duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.id = this.hashCode();
    }
}
