package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.services.UserService;

import java.time.LocalDate;

public class UserControllerTest {
    UserController userController;
    TestHelper testHelper;
    UserService userService;

    User user;
    User user1;

    @BeforeEach
    void BeforeEach () {
        userController = new UserController();
        testHelper = new TestHelper();
        userService = new UserService();
        user = testHelper.getUser().get(0);
        user1 = testHelper.getUser().get(1);
    }

    @Test
    public void shouldCreateUser() {
        userController.createUser(user);
        assertEquals(1, userController.findAllUsers().size());
    }

    @Test
    public void shouldUpdateUser() {
        userController.createUser(user);
        user1.setId(user.getId());
        userController.updateUser(user1);
        assertEquals("New User name", userController.findAllUsers().get(0).getName());
    }

    @Test
    public void shouldEqualNameAndLogin() {
        user.setName(null);
        userController.createUser(user);
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
}
