package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;

import java.time.LocalDate;
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
}
