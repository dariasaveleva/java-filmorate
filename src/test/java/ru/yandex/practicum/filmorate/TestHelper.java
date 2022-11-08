package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    public List<Film> getFilm() {
        Film film = Film.builder()
                .name("Film name")
                .description("Film description")
                .releaseDate(LocalDate.of(2022,2,10))
                .duration(90)
                .build();

        Film film1 = Film.builder()
                .id(film.getId())
                .name("New name")
                .description("Film description")
                .releaseDate(LocalDate.of(2022,2,10))
                .duration(90)
                .build();
        List<Film> films = new ArrayList<>();
        films.add(film);
        films.add(film1);

        return films;
    }

    public List<User> getUser() {
        User user = User.builder()
                .name("User name")
                .email("email@mail.ru")
                .login("login")
                .birthday(LocalDate.of(2000,2,10))
                .build();

        User user1 = User.builder()
                .id(user.getId())
                .name("New User name")
                .email("email@mail.ru")
                .login("login")
                .birthday(LocalDate.of(2000,2,10))
                .build();

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user1);

        return users;
    }


}
