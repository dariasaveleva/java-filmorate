package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @PositiveOrZero
    int id;
    @NotBlank
    @Email
    String email;
    @NotBlank
    String login;
    String name;
    LocalDate birthday;
}
