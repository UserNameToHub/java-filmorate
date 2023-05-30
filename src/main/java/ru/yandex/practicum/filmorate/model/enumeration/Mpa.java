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
public enum Mpa {
    G(1, "G"),
    PG(2, "PG"),
    PG13(3, "PG-13"),
    R(4, "R"),
    NC17(5, "NC-17"),
    UNKNOWN(6, "Unknown");

    private final int id;
    private final String name;

    @JsonCreator
    public static Mpa forValues(@JsonProperty("id") int id) {
        return Arrays.stream(Mpa.values())
                .filter(e -> e.id == id)
                .findFirst()
                .orElse(null);
    }
}
