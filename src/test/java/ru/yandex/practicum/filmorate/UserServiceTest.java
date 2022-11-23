package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
public class UserServiceTest {
    TestHelper testHelper;
    UserService userService;
    InMemoryUserStorage userStorage;

    User user;
    User user1;
    User user2;

    @BeforeEach
    void BeforeEach () {
        testHelper = new TestHelper();
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        user = testHelper.getUsers().get(0);
        user1 = testHelper.getUsers().get(1);
        user2 = testHelper.getUsers().get(2);
    }

    @Test
    public void shouldCreateUser() {
        userService.addUser(user);
        assertEquals(1, userService.getUsers().size());
    }

    @Test
    public void shouldUpdateUser() {
        userService.addUser(user);
        user1.setId(user.getId());
        userService.updateUser(user1);
        System.out.println(userService.getUsers());
        assertEquals("New User name", userService.getUsers().get(0).getName());
    }

    @Test
    public void shouldEqualNameAndLogin() {
        user.setName("");
        userService.addUser(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    public void shouldValidateBirthday() {
        user.setBirthday(LocalDate.now().plusYears(1));
        assertThrows(ValidationException.class, () -> userService.validateUser(user));
    }

    @Test
    public void shouldValidateLogin() {
        user.setLogin("login-new");
        assertThrows(ValidationException.class, () -> userService.validateUser(user));
    }

    @Test
    public void addFriend() {
        userService.addUser(user);
        userService.addUser(user1);
        userService.addFriend(user.getId(), user1.getId());
        assertEquals(1, userService.getUsersFriends(user.getId()).size());
        assertEquals(1, userService.getUsersFriends(user1.getId()).size());
    }

    @Test
    void removeFriend() {
        userService.addUser(user);
        userService.addUser(user1);
        userService.addFriend(user.getId(), user1.getId());
        userService.removeFriend(user.getId(), user1.getId());
        assertTrue(userService.getUsersFriends(user.getId()).isEmpty());
        assertTrue(userService.getUsersFriends(user1.getId()).isEmpty());
    }

    @Test
    void getUsersFriends() {
        userService.addUser(user);
        userService.addUser(user1);
        userService.addFriend(user.getId(), user1.getId());
        assertEquals(user1.getId(), userService.getUsersFriends(user.getId()).get(0));
    }

    @Test
    void getCommonFriends() {
        userService.addUser(user);
        userService.addUser(user1);
        userService.addUser(user2);
        userService.addFriend(user.getId(), user1.getId());
        userService.addFriend(user2.getId(), user1.getId());
        userService.getCommonFriends(user.getId(), user2.getId());
        assertEquals(user1, userService.getCommonFriends(user.getId(), user2.getId()).get(0));
    }
}
