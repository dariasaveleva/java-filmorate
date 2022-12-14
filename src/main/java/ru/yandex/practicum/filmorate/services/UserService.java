package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserDaoImpl userDao;

    @Autowired
    public UserService(UserDaoImpl userDao) {
        this.userDao = userDao;
    }

    public List<User> getUsers() {
        return userDao.getUsers();
    }

    public User getUser(int id) {
        checkUserExistence(id);
        return userDao.getById(id).orElseThrow();
    }

    public User addUser(User user) {
        log.info("Валидируем пользователя ");
        validateUser(user);
        log.info("Создаем пользователя.");
        return userDao.addUser(user);
    }

    public User updateUser(User user) {
        log.info("Валидируем пользователя ");
        checkUserExistence(user.getId());
        validateUser(user);
        log.info("Обновляем пользователя.");
        return userDao.update(user);
    }


    //добавление друзей
    public List<Integer> followFriend(int userId, int friendId) {
        checkUserExistence(userId);
        checkUserExistence(friendId);
        log.info("Пользователь c id {} отправил заявку на добавление пользователю с id {}",
                userId, friendId);
        return userDao.followFriend(userId, friendId);
    }

    public void deleteById(int userId) {
        userDao.deleteById(userId);
    }

    public List<Integer> unfollowFriend(int userId, int friendId) {
        checkUserExistence(userId);
        checkUserExistence(friendId);
        log.info("Пользователь c id {} удалил заявку на добавление к  пользователю с id {} ",
                userId, friendId);
        return userDao.unfollowFriend(userId, friendId);
    }

    public List<User> getUsersFriends(int userId) {
        checkUserExistence(userId);
        log.info("Выгружен список друзей пользователя {}", userId);
        return userDao.getUsersFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        checkUserExistence(userId);
        checkUserExistence(otherUserId);
        return userDao.getCommonFriends(userId, otherUserId);
    }

    public void validateUser(User user) {
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("День рождения не может быть в будущем");
        if (user.getLogin().contains("-"))
            throw new ValidationException("Логин не должен содержать пробелы");
        if (user.getName().equals("")) user.setName(user.getLogin());
    }

    public void checkUserExistence(int userId) {
        if (userDao.getById(userId).isEmpty()) {
            log.info("Запрашиваемый пользователь не найден");
            throw new NotFoundException("Пользователь не найден");
        }
    }
}
