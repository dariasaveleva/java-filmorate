package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

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

    @JsonIgnore
    private Set<Integer> usersLikes;
}
