package ru.yandex.practicum.filmorate.storage;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    int id = 0;

    private final HashMap<Integer, Film> films = new HashMap<>();

    @Override
    public Integer generateId() {
        return ++id;
    }

    @Override
    public Film saveFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilm(int filmId) {
        return films.get(filmId);
    }

    @Override
    public boolean isSaved(int filmId) {
        return films.containsKey(filmId);
    }
}
