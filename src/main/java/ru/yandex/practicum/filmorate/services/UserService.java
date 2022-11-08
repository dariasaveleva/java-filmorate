package ru.yandex.practicum.filmorate.services;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserService {
    private final HashMap<Integer, User> users = new HashMap<>();
    private int id = 0;

    private Integer generateId() {
        return ++id;
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User addUser(User user) {
        validateUser(user);
        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        if(!users.containsKey(user.getId())) throw
                new ValidationException("Невозможно обновить несуществующего пользователя");
        validateUser(user);
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
