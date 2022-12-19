package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
@AutoConfigureTestDatabase
public class UserDaoImplTest {
    UserDaoImpl userDao;
    User user;
    User user1;
    User user2;
    User friend;

    @Autowired
    public UserDaoImplTest(UserDaoImpl userDao) {
        this.userDao = userDao;
    }

    @BeforeEach
    void createUser () {
        user = new User(1, "mail@test.ru", "great", "Ulan",
                LocalDate.of(1995,11,20));
        user1 = new User(1, "gmail@test.ru", "barbi", "Kuto",
                LocalDate.of(1980,9,12));
        user2 = new User(3, "commonfriend@test.ru", "common", "Ktoto",
                LocalDate.of(1985,4,6));
        friend = new User(4, "friend@test.ru", "superfriend", " ",
                LocalDate.of(2005,1,1));
        userDao.addUser(user);
        userDao.addUser(friend);
        userDao.addUser(user2);
    }

    public User makeUserFromOptional (Optional<User> optionalUser) {
        return optionalUser.stream()
                .collect(Collectors.toList())
                .get(0);
    }

    @Test
    public void shouldCreateUser() {
        assertFalse(userDao.getUsers().isEmpty());
    }

    @Test
    public void shouldUpdateUser() {
        assertEquals("Kuto", userDao.update(user1).getName());
    }

    @Test
    public void followFriend() {
        assertEquals(List.of(user.getId(), friend.getId()), userDao.followFriend(user.getId(), friend.getId()));
    }

    @Test
    void unfollowFriend() {
        assertEquals(List.of(user.getId(), friend.getId()), userDao.unfollowFriend(user.getId(), friend.getId()));
    }

    @Test
    void getUsersFriends() {
        userDao.followFriend(user.getId(), friend.getId());;
        assertTrue(userDao.getUsersFriends(user.getId())
                .stream()
                .anyMatch(f -> f.getId() == friend.getId()));
    }

    @Test
    void getCommonFriends() {
        userDao.followFriend(user.getId(), friend.getId());
        userDao.followFriend(user2.getId(), friend.getId());
        assertTrue(userDao.getCommonFriends(user.getId(), user2.getId())
                .stream()
                .anyMatch(f -> f.getId() == friend.getId()));
    }
}
