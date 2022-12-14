package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.GenreDaoImpl;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.List;

@Service
@Slf4j
public class GenreService {
    private final GenreDaoImpl genreDao;

    @Autowired
    public GenreService(GenreDaoImpl genreDao) {
        this.genreDao = genreDao;
    }

    public List<Genre> findAll() {
        return genreDao.findAll();
    }

    public Genre getGenre(int id) {
        if (genreDao.getById(id).isEmpty()) {
            log.info("Запрошен несуществующий жанр");
            throw new NotFoundException("Не найден жанр");
        }
        return genreDao.getById(id).orElseThrow();
    }
}
