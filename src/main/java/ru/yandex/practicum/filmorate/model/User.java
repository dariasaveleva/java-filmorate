package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User {

    @NotNull
    int id;
    @NotBlank
    @Email
    String email;
    @NotBlank
    String login;
    String name;
    LocalDate birthday;

    @JsonIgnore
    private Set<Integer> friends;


}
