package ru.yandex.practicum.filmorate.model.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@RequiredArgsConstructor
public enum Genre {
    COMEDY(1, "Комедия"),
    DRAMA(2, "Драма"),
    CARTOON(3, "Мультфильм"),
    THRILLER(4, "Триллер"),
    DOCUMENTARY(5, "Документальный"),
    ACTION(6, "Боевик"),
    FANTASTIC(7, "Фантастика"),
    ADVENTURES(8, "Приключения"),
    SPORT(9, "Спорт"),
    HORRORS(10, "Ужасы"),
    UNKNOWN(11, "Unknown");

    private final int id;
    private final String name;

    @JsonCreator
    public static Genre forValues(@JsonProperty("id") int id) {
        return Arrays.stream(Genre.values())
                .filter(e -> e.id == id)
                .findFirst()
                .orElse(Genre.UNKNOWN);
    }
}
