package ru.yandex.practicum.filmorate.controllers;

import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.services.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    FilmService filmService = new FilmService();
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public List<Film> findAllFilms() {
        return filmService.getFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Создан фильм " + film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Обновлен фильм " + film);
        return filmService.updateFilm(film);
    }

}
