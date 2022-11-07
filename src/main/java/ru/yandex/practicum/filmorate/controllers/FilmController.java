package ru.yandex.practicum.filmorate.controllers;

import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    HashMap<Integer, Film> films = new HashMap<>();
    int id = 0;
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        film.setId(++id);
        films.put(film.getId(), film);
        log.info("Создан фильм " + film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if(!films.containsKey(film.getId())) throw
                new ValidationException("Невозможно обновить несуществующий фильм");
        validateFilm(film);
        log.info("Обновлен фильм " + film);
        films.put(film.getId(), film);
        return film;
    }

    public void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28)))
            throw new ValidationException("Дата фильма не может быть раньше 28 декабря 1985 года");
        if (film.getDuration() < 0)
            throw new ValidationException("Длительность фильма не может быть отрицательным числом");
//        films.values().stream().filter(film1 -> film1.getName().equals(film.getName())
//                && film1.getReleaseDate().equals(film.getReleaseDate()))
//                .forEach(film1 -> film);
    }
}
