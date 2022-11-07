package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    HashMap<Integer, User> users = new HashMap<>();
    int id = 1;

    @GetMapping
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validateUser(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Создан пользователь " + user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        if(!users.containsKey(user.getId())) throw
                new ValidationException("Невозможно обновить несуществующего пользователя");
        validateUser(user);
        log.info("Обновлен пользователь " + user);
        users.put(user.getId(), user);
        return user;
    }

    public void validateUser(User user) {
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("День рождения не может быть в будущем");
        if (user.getLogin().contains("-")) throw new ValidationException("Логин не должен содержать пробелы");
        if (user.getName()==null) user.setName(user.getLogin());
    }
}
