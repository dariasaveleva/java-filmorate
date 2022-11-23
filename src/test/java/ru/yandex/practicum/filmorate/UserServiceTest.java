package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.services.UserService;

import java.time.LocalDate;
public class UserServiceTest {
    TestHelper testHelper;
    UserService userService;
    User user;
    User user1;
    User user2;

    @BeforeEach
    void BeforeEach () {
        testHelper = new TestHelper();
        userService = testHelper.userService;
        testHelper.createUser();
        testHelper.createFriend();
        user = testHelper.user;
        user1 = testHelper.user1;
        user2 = testHelper.user2;
    }

    @Test
    public void shouldCreateUser() {
        assertEquals(3, userService.getUsers().size());
    }

    @Test
    public void shouldUpdateUser() {
        user2.setId(user.getId());
        userService.updateUser(user2);
        System.out.println(userService.getUsers());
        assertEquals("Name2", userService.getUsers().get(0).getName());
    }

    @Test
    public void shouldEqualNameAndLogin() {
        assertEquals(user1.getName(), user1.getLogin());
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
        assertEquals(1, userService.getUsersFriends(user.getId()).size());
        assertEquals(1, userService.getUsersFriends(user1.getId()).size());
    }

    @Test
    void removeFriend() {
        userService.removeFriend(user.getId(), user1.getId());
        assertTrue(userService.getUsersFriends(user.getId()).isEmpty());
        assertTrue(userService.getUsersFriends(user1.getId()).isEmpty());
    }

    @Test
    void getUsersFriends() {
        assertEquals(user1.getId(), userService.getUsersFriends(user.getId()).get(0));
    }

    @Test
    void getCommonFriends() {
        userService.addFriend(user2.getId(), user1.getId());
        userService.getCommonFriends(user.getId(), user2.getId());
        assertEquals(user1, userService.getCommonFriends(user.getId(), user2.getId()).get(0));
    }
}
