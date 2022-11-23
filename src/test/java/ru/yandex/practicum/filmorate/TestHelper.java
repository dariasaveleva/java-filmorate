package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TestHelper {

    public List<Film> getFilms() {
        Film film = Film.builder()
                .name("Film name")
                .description("Film description")
                .releaseDate(LocalDate.of(2022,2,10))
                .duration(90)
                .usersLikes(new HashSet<>())
                .build();

        Film film1 = Film.builder()
                .id(film.getId())
                .name("New name")
                .description("Film description")
                .releaseDate(LocalDate.of(2022,2,10))
                .duration(90)
                .usersLikes(new HashSet<>())
                .build();

        Film film2 = Film.builder()
                .name("New name")
                .description("Film description")
                .releaseDate(LocalDate.of(2022,2,10))
                .duration(90)
                .usersLikes(new HashSet<>())
                .build();
        List<Film> films = new ArrayList<>();
        films.add(film);
        films.add(film1);
        films.add(film2);

        return films;
    }

    public List<User> getUsers() {
        User user = User.builder()
                .name("User name")
                .email("email@mail.ru")
                .login("login")
                .birthday(LocalDate.of(2000,2,10))
                .friends(new HashSet<>())
                .build();

//        User user = new User(1,"email", "login", "name",
//                LocalDate.of(2000, 1, 15), new HashSet<>());

        User user1 = User.builder()
                .id(user.getId())
                .name("New User name")
                .email("newemail@mail.ru")
                .login("login")
                .birthday(LocalDate.of(2000,2,10))
                .friends(new HashSet<>())
                .build();

        User user2 = User.builder()
                .name("Name2")
                .email("email2@mail.ru")
                .login("login2")
                .birthday(LocalDate.of(2000,2,10))
                .friends(new HashSet<>())
                .build();

        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(user1);
        users.add(user2);

        return users;
    }


}
