package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmDao {
    Collection<Film> findAll();
    Film addFilm(Film film);
    Film update(Film film);
    Optional<Film> getById(int id);
    Optional<Film> deleteById(int id);
    Optional<Film> likeFilm(int filmId, int userId);
    Optional<Film> deleteLike(int filmId, int userId);
    List<Film> getPopularFilms(int count);
}
