package ru.yandex.practicum.filmorate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.Duration;
import java.time.LocalDate;

public class FilmControllerTest {
    FilmController filmController;
    Film film;
    Film film1;

    @BeforeEach
    void BeforeEach () {
        filmController = new FilmController();
        film = Film.builder()
                .name("Film name")
                .description("Film description")
                .releaseDate(LocalDate.of(2022,2,10))
                .duration(90)
                .build();
    }

    @Test
    public void shouldCreateFilm() {
        filmController.createFilm(film);
        assertEquals(1,filmController.findAllFilms().size());
    }

    @Test
    public void shouldUpdateFilm() {
        filmController.createFilm(film);
        film1 = Film.builder()
                .id(film.getId())
                .name("New name")
                .description("Film description")
                .releaseDate(LocalDate.of(2022,2,10))
                .duration(90)
                .build();
        filmController.updateFilm(film1);
        assertEquals("New name", filmController.findAllFilms().get(0).getName());
    }

    @Test
    public void shouldValidateReleaseDate() {
        film.setReleaseDate(LocalDate.of(1894,2,11));
        assertThrows(ValidationException.class, () -> filmController.validateFilm(film));
    }

    @Test
    public void shouldValidateDuration() {
        film.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmController.validateFilm(film));
    }

}
