package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getUsers() {
       String sql = "SELECT * FROM USERS";
       return jdbcTemplate.query(sql, this::makeUser);
    }

    @Override
    public User addUser(User user) {
        String sql = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "VALUES ( ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;}, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ?" +
                "WHERE USER_ID = ?";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public Optional<User> getById(int id) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
        SqlRowSet users = jdbcTemplate.queryForRowSet(sql, id);
        if (!users.next()) {
           return Optional.empty();
        }
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::makeUser, id));
    }

    @Override
    public Optional<User> deleteById(int id) {
        String deleteUser = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(deleteUser, id);
        return getById(id);
    }

    @Override
    public List<Integer> followFriend(int userId, int friendId) {
        String sql = "INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID) VALUES ( ?, ? )";
        jdbcTemplate.update(sql, userId, friendId);
        return List.of(userId, friendId);
    }

    @Override
    public List<Integer> unfollowFriend(int userId, int friendId) {
        String sql = "DELETE FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sql, userId, friendId);
        return List.of(userId, friendId);
    }

    @Override
    public List<User> getUsersFriends(int id) {
        String sql = "SELECT USERS.* FROM USERS " +
                "LEFT JOIN FRIENDSHIP f ON USERS.USER_ID = f.FRIEND_ID " +
                "WHERE f.USER_ID = ?";
        return jdbcTemplate.query(sql, this::makeUser, id);
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherUserId) {
        String sql = "SELECT u.* FROM USERS u, FRIENDSHIP f1, FRIENDSHIP f2 " +
                "WHERE f1.USER_ID = ? AND f2.USER_ID = ?" +
                "AND f1.FRIEND_ID = f2.FRIEND_ID " +
                "AND f1.FRIEND_ID = u.USER_ID";

        return jdbcTemplate.query(sql, this::makeUser, userId, otherUserId);
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return new User(id, email, login, name, birthday);
    }
}
