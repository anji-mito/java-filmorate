package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component

public class UserDbStorage implements UserStorage {
    final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        long id = simpleJdbcInsert.executeAndReturnKey(UserMapper.toMap(user)).longValue();
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "update users set " +
                "login = ?, name = ?, birthday = ?, email = ?" +
                "where id = ?";
        jdbcTemplate.update(sqlQuery, user.getLogin(), user.getName(), user.getBirthday(),
                user.getEmail(), user.getId());
        return user;
    }

    @Override
    public void remove(User user) {
        String sqlQuery = "delete from users where id = ?";
        jdbcTemplate.update(sqlQuery, user.getId());
    }

    @Override
    public List<Optional<User>> findAllUsers() {
        String sqlQuery = "select id, login, name, birthday, email from users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public Optional<User> findUser(Long userId) {
        try {
            String sqlQuery = "select id, name, login, birthday, email " +
                    "from users where id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, userId);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Long> getFriends(Long userId) {
        try {
            String sqlQuery = "SELECT FRIEND_ID \n"
                    + "FROM FRIENDSHIP " +
                    "where user_id = ?";
            return jdbcTemplate.query(sqlQuery, this::mapRowToIds, userId);
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }
    }

    @Override
    public void addFriend(long id, long friendId) {
        String sqlQuery = "insert into FRIENDSHIP(USER_ID, FRIEND_ID) " +
                "values (?, ?)";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public void removeFriend(long id, long friendId) {
        String sqlQuery = "delete from FRIENDSHIP where friend_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQuery, friendId, id);
    }

    private Long mapRowToIds(ResultSet resultSet, int i) throws SQLException {
        return resultSet.getLong("friend_id");
    }

    private Optional<User> mapRowToUser(ResultSet resultSet, int i) throws SQLException {
        return Optional.of(User.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .email(resultSet.getString("email"))
                .build());
    }
}
