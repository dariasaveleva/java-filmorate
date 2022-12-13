package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final InMemoryFilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        log.info("Проверка существования фильма.");
        validateExistenceForAdd(film.getId());
        log.info("Проверка даты и продолжительности фильма.");
        validateFilm(film);
        log.info("Сохраняем фильм.");
        film.setUsersLikes(new HashSet<>());
        filmStorage.saveFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        log.info("Проверка существования фильма.");
        validateNonExistence(film.getId());
        log.info("Проверка даты и продолжительности фильма.");
        validateFilm(film);
        log.info("Обновляем фильм.");
        film.setUsersLikes(new HashSet<>());
        filmStorage.updateFilm(film);
        return film;
    }

    public void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28)))
            throw new ValidationException("Дата фильма не может быть раньше 28 декабря 1985 года");
        if (film.getDuration() < 0)
            throw new ValidationException("Длительность фильма не может быть отрицательным числом");
    }

    public void validateExistenceForAdd(int filmId) {
        if (filmStorage.isSaved(filmId))
            throw new ValidationException("Фильм уже существует. Для обновления используйте запрос PUT");
    }

    public void validateNonExistence(int filmId) {
        if (!filmStorage.isSaved(filmId))
            throw new NotFoundException("Такой фильм не существует.");
    }

    public Film getFilm(int id) {
        validateNonExistence(id);
        return filmStorage.getFilm(id);
    }

    public Film likeFilm(int filmId, int userId) {
        validateNonExistence(filmId);
        Film film = filmStorage.getFilm(filmId);
        film.getUsersLikes().add(userId);
        return film;
    }

    public void validateLikeExistence(int filmId, int userId) {
        Film film = filmStorage.getFilm(filmId);
        if (!film.getUsersLikes().contains(userId))
            throw new NotFoundException("Такой лайк не существует.");
    }

    public Film deleteLike(int filmId, int userId) {
        validateNonExistence(filmId);
        Film film = filmStorage.getFilm(filmId);
        validateLikeExistence(filmId,userId);
        film.getUsersLikes().remove(userId);
        return film;
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted((o1, o2) -> o2.getUsersLikes().size() - o1.getUsersLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

}
