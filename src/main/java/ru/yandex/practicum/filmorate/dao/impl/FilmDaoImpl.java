package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final MpaDaoImpl mpaDaoImpl;

    @Autowired
    public FilmDaoImpl (JdbcTemplate jdbcTemplate,
                        NamedParameterJdbcTemplate namedParameterJdbcTemplate,
                        MpaDaoImpl mpaDaoImpl) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.mpaDaoImpl = mpaDaoImpl;
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT film.*, mpa.* FROM FILM " +
                "JOIN MPA ON mpa.mpa_id = film.mpa_id";
        List<Film> films = jdbcTemplate.query(sql, this::makeFilm);
        loadGenres(films);
        return films;
    }

    @Override
    public Film addFilm(Film film) {
       String sql = "INSERT INTO FILM (name, description, release_date, duration, mpa_id) " +
               "VALUES (?, ?, ?, ?, ?)";
       KeyHolder keyHolder = new GeneratedKeyHolder();
       jdbcTemplate.update(connection -> {
           PreparedStatement filmst = connection.prepareStatement(sql, new String[]{"film_id"});
           filmst.setString(1, film.getName());
           filmst.setString(2, film.getDescription());
           filmst.setDate(3, Date.valueOf(film.getReleaseDate()));
           filmst.setLong(4, film.getDuration());
           filmst.setInt(5, film.getMpa().getId());
        return filmst;}, keyHolder);
       film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
       film.setMpa(mpaDaoImpl.getById(film.getMpa().getId()).orElseThrow());

       String genreSql = "INSERT INTO FILM_GENRE (film_id, genre_id) VALUES (?,?)";
       if (film.getGenres() != null) {
           film.getGenres().stream()
                   .map(genre -> jdbcTemplate.update(genreSql, film.getId(), genre.getId()))
                   .collect(Collectors.toList());
           film.getGenres().clear();
           loadGenres(Collections.singletonList(film));
       } else film.setGenres(Collections.emptyList());
       return film;
    }


    @Override
    public Film update(Film film) {
        String sql = "UPDATE FILM SET name = ?, description = ?, release_date = ?, " +
                "duration = ?, mpa_id = ? WHERE film_id = ?";
        film.setMpa(mpaDaoImpl.getById(film.getMpa().getId()).orElseThrow());
        if (film.getGenres() != null) {
            String delete = "DELETE FROM FILM_GENRE WHERE film_id = ?";
            String update = "INSERT INTO FILM_GENRE (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.update(delete, film.getId());
            for (Genre g: film.getGenres()) {
                String checkExistence = "SELECT * FROM FILM_GENRE WHERE film_id = ? AND genre_id = ?";
                SqlRowSet check = jdbcTemplate.queryForRowSet(checkExistence, film.getId(), g.getId());
                if (!check.next()) {
                    jdbcTemplate.update(update, film.getId(), g.getId());
                }
            }
            film.getGenres().clear();
            loadGenres(Collections.singletonList(film));
        } else film.setGenres(Collections.emptyList());
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());

        return film;
    }

    @Override
    public Optional<Film> getById(int id) {
        String sql = "SELECT film.*, mpa.* FROM FILM " +
                "JOIN mpa ON mpa.mpa_id = film.mpa_id WHERE film.film_id = ?";
        SqlRowSet films = jdbcTemplate.queryForRowSet(sql, id);
        if (!films.next()) {
            return Optional.empty();
        }
        Film film = jdbcTemplate.queryForObject(sql, this::makeFilm, id);
        loadGenres(Collections.singletonList(film));
        return Optional.ofNullable(film);
    }

    @Override
    public Optional<Film> deleteById(int id) {
        Optional<Film> film = getById(id);
        String deleteGenre = "DELETE FROM film WHERE film_id = ?";
        String deleteFilm = "DELETE FROM film WHERE film_id = ?";
        jdbcTemplate.update(deleteGenre, id);
        jdbcTemplate.update(deleteFilm, id);
        return film;
    }

    @Override
    public Optional<Film> likeFilm(int filmId, int userId) {
        String addLikeSql = "INSERT INTO LIKES (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(addLikeSql, filmId, userId);
        return getById(filmId);
    }

    @Override
    public Optional<Film> deleteLike(int filmId, int userId) {
        Optional<Film> film = getById(filmId);
        String deleteLike = "DELETE FROM LIKES WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(deleteLike, filmId, userId);
        return film;
    }

    @Override
    public List<Film> getPopularFilms(int count){
        String sql = "SELECT film.film_id, film.name, description, release_date, duration, m.mpa_id," +
                "m.name FROM FILM " +
                "LEFT JOIN LIKES l ON film.film_id = l.film_id " +
                "JOIN MPA m ON film.mpa_id = m.mpa_id " +
                "GROUP BY film.film_id, l.film_id IN (SELECT film_id FROM LIKES) " +
                "ORDER BY COUNT(l.film_id) DESC LIMIT ?";
        List<Film> films = jdbcTemplate.query(sql, this::makeFilm, count);
        loadGenres(films);
        return films;
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("film_id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        long Duration = rs.getLong("duration");
        Mpa mpa = new Mpa(rs.getInt("mpa.mpa_id"), rs.getString("mpa.name"));
        return new Film(id, name, description,releaseDate, Duration, mpa, new ArrayList<>());
    }

    private void loadGenres(List<Film> films) {
       String sql = "SELECT film_id, g2.* FROM FILM_GENRE " +
                "join GENRE g2 on g2.genre_id = FILM_GENRE.genre_id " +
                "where film_id IN (:ids)";

        List <Integer> ids = films.stream()
                .map(Film::getId)
                .collect(Collectors.toList());

        Map<Integer, Film> filmMap = films.stream()
                .collect(Collectors.toMap(Film::getId, film -> film, (a,b) -> b));

        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        SqlRowSet sqlRowSet = namedParameterJdbcTemplate.queryForRowSet(sql, parameters);
        while (sqlRowSet.next()) {
            int filmId = sqlRowSet.getInt("film_id");
            int genreId = sqlRowSet.getInt("genre_id");
            String name = sqlRowSet.getString("name");
            filmMap.get(filmId).getGenres().add(new Genre(genreId, name));
        }
        films.stream()
                .map(film -> film.getGenres().addAll(filmMap.get(film.getId()).getGenres()));
    }
}
