package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public List<User> findAllUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Создан пользователь " + user);
        return userService.addUser(user);
    }

    @GetMapping("{id}")
    public User findUser(@PathVariable int id) {
        return userService.getUser(id);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public List<Integer> followFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.followFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public List<Integer> unfollowFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.unfollowFriend(id, friendId);
    }
    @DeleteMapping("{userId}")
    public void deleteById(@PathVariable int userId) {
        userService.deleteById(userId);
    }
    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        return userService.getUsersFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }

}
