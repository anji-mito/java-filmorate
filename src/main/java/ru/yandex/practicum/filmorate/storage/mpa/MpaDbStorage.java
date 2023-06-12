package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Mpa> findMpa(Long id) {
        try {
            String sqlQuery = "SELECT ID, NAME FROM Mpa   where id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Mpa create(Mpa mpa) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        long id = simpleJdbcInsert.executeAndReturnKey(mpa.toMap()).longValue();
        mpa.setId((int) id);
        return mpa;
    }

    @Override
    public Mpa update(Mpa mpa) {
        String sqlQuery = "update mpa set name = ? where id = ?";
        jdbcTemplate.update(sqlQuery
                , mpa.getName()
                , mpa.getId());
        return mpa;
    }

    @Override
    public void remove(Mpa mpa) {
        String sqlQuery = "delete from mpa where id = ?";
        jdbcTemplate.update(sqlQuery, mpa.getId());
    }

    @Override
    public List<Optional<Mpa>> findAll() {
        String sqlQuery = "SELECT * FROM MPA";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    private Optional<Mpa> mapRowToMpa(ResultSet resultSet, int i) throws SQLException {
        return Optional.of(Mpa.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build());
    }
}
