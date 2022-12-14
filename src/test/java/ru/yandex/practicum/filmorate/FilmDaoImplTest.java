package ru.yandex.practicum.filmorate;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.FilmDaoImpl;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureTestDatabase
public class FilmDaoImplTest {

    FilmDaoImpl filmDao;
    UserDaoImpl userDao;
    Film film;
    Film film1;
    Film film2;
    User user;
    User user1;

    @Autowired
    public FilmDaoImplTest (FilmDaoImpl filmDao, UserDaoImpl userDao) {
        this.filmDao = filmDao;
        this.userDao = userDao;
    }

    @BeforeEach
    void createFilm() {
        film = new Film(1, "name", "description",
                LocalDate.of(2022,11,2), 90,
                new Mpa(1, "PG"), null);
        film1 = new Film(1, "new name", "new description",
                LocalDate.of(2022,11,2), 90,
                new Mpa(1, "PG"), null);
        film2 = new Film(3, "Harry Potter", "A history about a Boy who lived",
                LocalDate.of(2001,11,4), 120,
                new Mpa(1, "PG"), null);
        filmDao.addFilm(film);
        filmDao.addFilm(film2);
        user = new User(1, "mail@test.ru", "great", "Ulan",
                LocalDate.of(1995,11,20));
        user1 = new User(2, "gmail@test.ru", "barbi", "Kuto",
                LocalDate.of(1980,9,12));
        userDao.addUser(user);
        userDao.addUser(user1);
    }

    public Film makeFilmFromOptional (Optional<Film> optionalFilm) {
        return optionalFilm.stream()
                .collect(Collectors.toList())
                .get(0);
    }

    @Test
    public void shouldFindAllFilms() {
        assertFalse(filmDao.findAll().isEmpty());
    }

    @Test
    public void shouldCreateFilm() {
        assertEquals(film.getName(), makeFilmFromOptional(filmDao.getById(film.getId())).getName());
    }

    @Test
    public void shouldUpdateFilm() {
        assertEquals("new name", filmDao.update(film1).getName());
    }

    @Test
    public void getFilmByIdTest() {
        assertEquals(film.getName(), makeFilmFromOptional(filmDao.getById(film.getId())).getName());
    }

    @Test
    public void likeFilmTest() {
        assertEquals(film.getName(), makeFilmFromOptional(filmDao.likeFilm(film.getId(), user.getId())).getName());
}
    @Test
    public void deleteLikeTest() {
        filmDao.likeFilm(film.getId(), user.getId());
        filmDao.deleteLike(film.getId(), user.getId());
        assertEquals(film.getName(), makeFilmFromOptional(filmDao.likeFilm(film.getId(), user.getId())).getName());
    }

     @Test
    public void getPopularFilmsTest() {
        filmDao.likeFilm(film.getId(), user.getId());
        filmDao.likeFilm(film.getId(), user1.getId());
        filmDao.likeFilm(film2.getId(), user.getId());
        assertEquals(film.getName(), filmDao.getPopularFilms(2).get(0).getName());
    }
}