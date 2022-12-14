package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    private Mpa mpa;
    private List<Genre> genres;
}
