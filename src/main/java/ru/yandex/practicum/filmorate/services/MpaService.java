package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.MpaDaoImpl;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@Slf4j
public class MpaService {
    private final MpaDaoImpl mpaDao;

    @Autowired
    public MpaService(MpaDaoImpl mpaDao) {
        this.mpaDao = mpaDao;
    }

    public List<Mpa> findAll() {
        return mpaDao.findAll();
    }

    public Mpa getMpa(int id) {
        if (mpaDao.getById(id).isEmpty()) {
            throw new NotFoundException("Mpa не найден");
        }
        return mpaDao.getById(id).orElseThrow();
    }
}
