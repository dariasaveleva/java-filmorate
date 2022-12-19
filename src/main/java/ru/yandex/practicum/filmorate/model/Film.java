package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {
    @NonNull
    int id;
    @NotBlank
    String name;
    @Size(min=1, max = 200)
    String description;
    LocalDate releaseDate;
    long duration;
    Mpa mpa;
    List<Genre> genres;
}
