package ru.yandex.practicum.filmorate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.services.UserService;

import java.time.LocalDate;

public class FilmServiceTest {
    FilmService filmService;
    UserService userService;
    TestHelper testHelper;

    @BeforeEach
    void BeforeEach () {
        testHelper = new TestHelper();
        filmService = testHelper.filmService;
        userService = testHelper.userService;
        testHelper.createFilm();
        testHelper.createUser();
    }

    @Test
    public void shouldCreateFilm() {
        assertEquals(2,filmService.getFilms().size());
    }

    @Test
    public void addExistedFilmTest() {
        assertThrows(ValidationException.class, () -> filmService.addFilm(testHelper.film));
    }

    @Test
    public void shouldUpdateFilm() {
        testHelper.film1.setId(testHelper.film.getId());
        filmService.updateFilm(testHelper.film1);
        assertEquals("New name", filmService.getFilms().get(0).getName());
    }

    @Test
    public void updateNotExistedFilmTest() {
        assertThrows(NotFoundException.class, () -> filmService.updateFilm(testHelper.film2));
    }

    @Test
    public void shouldValidateReleaseDate() {
        testHelper.film.setReleaseDate(LocalDate.of(1894,2,11));
        assertThrows(ValidationException.class, () -> filmService.validateFilm(testHelper.film));
    }

    @Test
    public void shouldValidateDuration() {
        testHelper.film.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmService.validateFilm(testHelper.film));
    }

    @Test
    public void getFilmByIdTest() {
        assertEquals(testHelper.film.getName(),filmService.getFilm(testHelper.film.getId()).getName());
    }

    @Test
    public void likeFilmTest() {
        filmService.likeFilm(testHelper.film.getId(), testHelper.user.getId());
        assertEquals(1, testHelper.film.getUsersLikes().size());
    }
    @Test
    public void deleteLikeTest() {
        filmService.likeFilm(testHelper.film.getId(), testHelper.user.getId());
        filmService.deleteLike(testHelper.film.getId(), testHelper.user.getId());
        assertFalse(testHelper.film.getUsersLikes().contains(testHelper.user.getId()));
    }

    @Test
    public void getPopularFilmsTest() {
        filmService.likeFilm(testHelper.film.getId(), testHelper.user.getId());
        assertEquals(testHelper.film.getName(), filmService.getPopularFilms(1).get(0).getName());
    }
}