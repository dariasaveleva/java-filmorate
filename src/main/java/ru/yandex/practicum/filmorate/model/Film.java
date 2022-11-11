package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;

import java.time.Duration;
import java.time.LocalDate;
@Data
@Builder
public class Film {
    @NonNull
    private int id;
    @NotBlank
    private String name;
    @Size(min=1, max = 200)
    private String description;
    private LocalDate releaseDate;
    private long duration;
}
