package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> getUsers();
    User addUser(User user);
    User update(User user);
    Optional<User> getById(int id);
    Optional<User> deleteById(int id);
    List<Integer> followFriend(int userId, int friendId);
    List<Integer> unfollowFriend(int userId, int friendId);
    List<User> getUsersFriends(int id);
    List<User> getCommonFriends(int userId, int otherUserId);
}
