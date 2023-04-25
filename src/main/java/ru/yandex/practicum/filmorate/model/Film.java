package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.deserializer.CustomDurationDeserializer;
import ru.yandex.practicum.filmorate.validation.annotation.ReleaseDateConstraint;
import ru.yandex.practicum.filmorate.serializer.CustomDurationSerializer;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    @NotNull(message = "Id не может быть пустым.")
    private Integer id;

    @NotEmpty(message = "Название не может быть пустым")
    @NotNull(message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов.")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ReleaseDateConstraint(from = "1895-12-28", message = "Дата релиза — не раньше 28 декабря 1895 года.")
    private LocalDate releaseDate;

    @JsonDeserialize(using = CustomDurationDeserializer.class)
    @JsonSerialize(using = CustomDurationSerializer.class)
    @DurationMin(nanos = 1, message = "Продолжительность фильма не может быть меньше или равно нулю.")
    private Duration duration;
}
