package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class FilmsGenresDbStorage implements FilmsGenresStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmsGenresDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenresOfFilm(long id) {
        String sqlQuery = "SELECT FG.genre_id as genre_id, g.name as name FROM FILMS_GENRES"
                + " as FG JOIN GENRES as G on g.id = fg.genre_id WHERE FG.FILM_ID = ?";
        List<Optional<Genre>> optionalList = jdbcTemplate.query(sqlQuery, this::mapRowToGenres, id);
        List<Genre> result = new ArrayList<>();
        for(Optional<Genre> optionalGenre: optionalList) {
            optionalGenre.ifPresent(result::add);
        }
        return result;
    }

    private Optional<Genre> mapRowToGenres(ResultSet resultSet, int i) throws SQLException {
        return Optional.of(Genre.builder()
                .id(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name"))
                .build());
    }
}
