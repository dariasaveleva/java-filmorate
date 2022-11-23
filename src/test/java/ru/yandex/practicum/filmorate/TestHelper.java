package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TestHelper {

    FilmService filmService;
    UserService userService;
    InMemoryFilmStorage filmStorage;
    InMemoryUserStorage userStorage;
    Film film;
    Film film1;
    Film film2;
    User user;
    User user1;
    User user2;


    public TestHelper() {
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(filmStorage);
        userService = new UserService(userStorage);
        film = getFilms().get(0);
        film1 = getFilms().get(1);
        film2 = getFilms().get(2);
        user = getUsers().get(0);
        user1 = getUsers().get(1);
        user2 = getUsers().get(2);
    }

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
                .description("Film description1")
                .releaseDate(LocalDate.of(2022,2,10))
                .duration(90)
                .usersLikes(new HashSet<>())
                .build();

        Film film2 = Film.builder()
                .name("New name2")
                .description("Film description2")
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

        User user1 = User.builder()
                .id(user.getId())
                .name("")
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

    public void createFilm() {
        filmService.addFilm(film);
        filmService.addFilm(film1);
    }

    public void createUser() {
        userService.addUser(user);
        userService.addUser(user1);
        userService.addUser(user2);
    }

    public void createFriend() {
        userService.addFriend(user.getId(), user1.getId());
    }
}
