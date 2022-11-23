package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User getUser(int id) {
        if (userStorage.getUser(id) == null) {
            throw new NotFoundException("Пользователь не существует.");
        }
        return userStorage.getUser(id);
    }

    public User addUser(User user) {
        log.info("Проверяем, не существует ли такой пользователь");
        validateUserBeforeAdd(user.getId());
        log.info("Валидируем пользователя ");
        validateUser(user);
        log.info("Создаем пользователя.");
        user.setFriends(new HashSet<>());
        userStorage.saveUser(user);
        return user;
    }

    public User updateUser(User user) {
        log.info("Проверяем, существует ли такой пользователь");
        validateNonExistence(user.getId());
        log.info("Валидируем пользователя ");
        validateUser(user);
        log.info("Обновляем пользователя.");
        user.setFriends(new HashSet<>());
        userStorage.updateUser(user);
        return user;
    }

    public void validateUser(User user) {
        if (user.getBirthday().isAfter(LocalDate.now()))
            throw new ValidationException("День рождения не может быть в будущем");
        if (user.getLogin().contains("-"))
            throw new ValidationException("Логин не должен содержать пробелы");
        if (user.getName().equals("")) user.setName(user.getLogin());
    }

    public void validateUserBeforeAdd(int userId) {
         if (userStorage.isSaved(userId))
             throw new ValidationException("Пользователь уже существует. " +
                     "Для обновления используйте запрос PUT");
    }

    public void validateNonExistence(int userId) {
        if (!(userStorage.isSaved(userId)))
            throw new NotFoundException("Пользователь не существует.");
    }

    //добавление друзей
    public List<User> addFriend(int userId, int friendId) {
        log.info("Проверяем, существует ли такой пользователь");
        validateNonExistence(userId);
        validateNonExistence(friendId);
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.info("Пользователь {} и пользователь {} добавлены в друзья друг к другу", user, friend);
        return List.of(user, friend);
    }

    public List<User> removeFriend(int userId, int friendId) {
        User user1 = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (!(user1.getFriends().contains(friendId)))
            throw new RuntimeException();
        user1.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.info("Пользователь {} и пользователь {} удалены из друзей друг друга", user1, friend);
        return List.of(user1, friend);
    }

    public List<Integer> getUsersFriends(int userId) {
        log.info("Пользователь {} и пользователь {} добавлены в друзья друг к другу");
        return userStorage.getUser(userId).getFriends().stream()
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        User user = userStorage.getUser(userId);
        User otherUser = userStorage.getUser(otherUserId);

        return user.getFriends().stream()
                .filter(friendId -> otherUser.getFriends().contains(friendId))
                .map(userStorage::getUser)
                .collect(Collectors.toList());
    }

}
