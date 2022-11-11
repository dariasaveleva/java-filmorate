package ru.yandex.practicum.filmorate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.services.FilmService;

import java.time.LocalDate;

public class FilmControllerTest {
    FilmController filmController;
    FilmService filmService;
    TestHelper testHelper;
    Film film;
    Film film1;

    @BeforeEach
    void BeforeEach () {
        filmController = new FilmController();
        testHelper = new TestHelper();
        filmService = new FilmService();
        film = testHelper.getFilm().get(0);
        film1 = testHelper.getFilm().get(1);
    }

    @Test
    public void shouldCreateFilm() {
        filmController.createFilm(film);
        assertEquals(1,filmController.findAllFilms().size());
    }

    @Test
    public void shouldUpdateFilm() {
        filmController.createFilm(film);
        film1.setId(film.getId());
        filmController.updateFilm(film1);
        assertEquals("New name", filmController.findAllFilms().get(0).getName());
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

}
