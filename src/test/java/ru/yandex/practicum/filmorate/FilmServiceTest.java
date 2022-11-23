package ru.yandex.practicum.filmorate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

public class FilmServiceTest {
    FilmService filmService;
    UserService userService;
    TestHelper testHelper;
    InMemoryFilmStorage filmStorage;
    InMemoryUserStorage userStorage;

    Film film;
    Film film1;
    Film film2;
    User user;

    @BeforeEach
    void BeforeEach () {
        testHelper = new TestHelper();
        filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        filmService = new FilmService(filmStorage);
        userService = new UserService(userStorage);
        film = testHelper.getFilms().get(0);
        film1 = testHelper.getFilms().get(1);
        film2 = testHelper.getFilms().get(2);
        user = testHelper.getUsers().get(0);
    }

    @Test
    public void shouldCreateFilm() {
        filmService.addFilm(film);
        filmService.addFilm(film2);
        assertEquals(2,filmService.getFilms().size());
    }

    @Test
    public void addExistedFilmTest() {
        filmService.addFilm(film);
        assertThrows(ValidationException.class, () -> filmService.addFilm(film));
    }

    @Test
    public void shouldUpdateFilm() {
        filmService.addFilm(film);
        film1.setId(film.getId());
        filmService.updateFilm(film1);
        assertEquals("New name", filmService.getFilms().get(0).getName());
    }

    @Test
    public void updateNotExistedFilmTest() {
        assertThrows(NotFoundException.class, () -> filmService.updateFilm(film));
    }

    @Test
    public void shouldValidateReleaseDate() {
        film.setReleaseDate(LocalDate.of(1894,2,11));
        assertThrows(ValidationException.class, () -> filmService.validateFilm(film));
    }

    @Test
    public void shouldValidateDuration() {
        film.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmService.validateFilm(film));
    }

    @Test
    public void getFilmByIdTest() {
        filmService.addFilm(film);
        filmService.addFilm(film1);
        assertEquals(film.getName(),filmService.getFilm(film.getId()).getName());
    }

    @Test
    public void likeFilmTest() {
        userService.addUser(user);
        filmService.addFilm(film);
        filmService.likeFilm(film.getId(), user.getId());
        assertEquals(1, film.getUsersLikes().size());
    }
    @Test
    public void deleteLikeTest() {
        userService.addUser(user);
        filmService.addFilm(film);
        filmService.likeFilm(film.getId(), user.getId());
        filmService.deleteLike(film.getId(), user.getId());
        assertFalse(film.getUsersLikes().contains(user.getId()));
    }

    @Test
    public void getPopularFilmsTest() {
        filmService.addFilm(film);
        filmService.addFilm(film1);
        userService.addUser(user);
        filmService.likeFilm(film.getId(), user.getId());
        assertEquals(film.getName(), filmService.getPopularFilms(1).get(0).getName());
    }



}