package ru.yandex.practicum.filmorate.services;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilmService {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 0;

    private Integer generateId() {
        return ++id;
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        if(!films.containsKey(film.getId())) throw
                new ValidationException("Невозможно обновить несуществующий фильм");
        validateFilm(film);
        films.put(film.getId(), film);
        return film;
    }

    public void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28)))
            throw new ValidationException("Дата фильма не может быть раньше 28 декабря 1985 года");
        if (film.getDuration() < 0)
            throw new ValidationException("Длительность фильма не может быть отрицательным числом");
    }
}
