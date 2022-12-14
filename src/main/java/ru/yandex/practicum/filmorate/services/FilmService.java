package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmDaoImpl filmDao;
    private final UserDaoImpl userDao;

    @Autowired
    public FilmService(FilmDaoImpl filmDao, UserDaoImpl userDao) {
        this.filmDao = filmDao;
        this.userDao = userDao;
    }

    public List<Film> getFilms() {
        log.info("Выгружен список всех фильмов");
        return filmDao.findAll();
    }

    public Film addFilm(Film film) {
        log.info("Проверка даты и продолжительности фильма.");
        validateFilm(film);
        log.info("Сохраняем фильм.");
        return filmDao.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Проверка даты и продолжительности фильма.");
        validateFilm(film);
        checkFilmExistence(film.getId());
        log.info("Обновляем фильм.");
        return filmDao.update(film);
    }

    public void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28)))
            throw new ValidationException("Дата фильма не может быть раньше 28 декабря 1985 года");
        if (film.getDuration() < 0)
            throw new ValidationException("Длительность фильма не может быть отрицательным числом");
    }

    public Film getFilm(int id) {
        return filmDao.getById(id).orElseThrow(() -> {
            log.warn("Запрошен несуществующий фильм");
            throw new NotFoundException("Запрашиваемый фильм отсутствует");
        });
    }

    public Film likeFilm(int filmId, int userId) {
        if (userDao.getById(userId).isEmpty()) {
            throw new NotFoundException("Такой пользователь не существует.");
        }
        return filmDao.likeFilm(filmId, userId).orElseThrow();
    }


    public Film deleteLike(int filmId, int userId) {
        if (userDao.getById(userId).isEmpty()) {
            throw new NotFoundException("Такой пользователь не существует.");
        }
        checkFilmExistence(filmId);
        log.info("Лайк удалён");
        return filmDao.deleteLike(filmId, userId).orElseThrow();
    }

    public List<Film> getPopularFilms(int count) {
        log.info("Выгружаем список самых популярных фильмов");
        return filmDao.getPopularFilms(count);
    }

    public void checkFilmExistence(int id) {
        if (filmDao.getById(id).isEmpty()) {
            log.info("Запрашиваемый пользователь не найден");
            throw new NotFoundException("Пользователь не найден");
        }
    }
}
