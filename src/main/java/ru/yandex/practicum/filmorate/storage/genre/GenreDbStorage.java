package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> findGenre(Long id) {
        try {
            String sqlQuery = "SELECT ID, NAME FROM genres where id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Genre create(Genre genre) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("genres")
                .usingGeneratedKeyColumns("id");
        long id = simpleJdbcInsert.executeAndReturnKey(genre.toMap()).longValue();
        genre.setId((int) id);
        return genre;
    }

    @Override
    public Genre update(Genre genre) {
        String sqlQuery = "update genres set name = ? where id = ?";
        jdbcTemplate.update(sqlQuery
                ,genre.getName()
                ,genre.getId());
        return genre;
    }

    @Override
    public void remove(Genre genre) {
        String sqlQuery = "delete from genres where id = ?";
        jdbcTemplate.update(sqlQuery, genre.getId());
    }

    @Override
    public List<Optional<Genre>> findAll() {
        String sqlQuery = "SELECT * FROM GENRES";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }
    private Optional<Genre> mapRowToGenre(ResultSet resultSet, int i) throws SQLException {
        return Optional.of(Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build());
    }
}
