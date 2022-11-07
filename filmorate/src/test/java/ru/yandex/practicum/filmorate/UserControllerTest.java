package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.ValidationException;

import java.time.Duration;
import java.time.LocalDate;

public class UserControllerTest {
   UserController userController;
    User user;
    User user1;

    @BeforeEach
    void BeforeEach () {
        userController = new UserController();
        user = User.builder()
                .name("User name")
                .email("email@mail.ru")
                .login("login")
                .birthday(LocalDate.of(2000,2,10))
                .build();
    }

    @Test
    public void shouldCreateUser() {
        userController.createUser(user);
        assertEquals(1, userController.findAllUsers().size());
    }

    @Test
    public void shouldUpdateUser() {
        userController.createUser(user);
        user1 = User.builder()
                .id(user.getId())
                .name("New User name")
                .email("email@mail.ru")
                .login("login")
                .birthday(LocalDate.of(2000,2,10))
                .build();
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
        assertThrows(ValidationException.class, () -> userController.validateUser(user));
    }

    @Test
    public void shouldValidateLogin() {
        user.setLogin("login-new");
        assertThrows(ValidationException.class, () -> userController.validateUser(user));
    }
}
