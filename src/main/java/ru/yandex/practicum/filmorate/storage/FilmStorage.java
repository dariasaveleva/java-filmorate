package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Integer generateId();
    Film saveFilm(Film film);
    List<Film> getAllFilms();
    Film updateFilm(Film film);

    Film getFilm(int filmId);

    boolean isSaved(int filmId);
}
