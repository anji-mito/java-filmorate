package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Film> findFilm(Long id) {
        try {
            String sqlQuery = "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION,"
                    + "                f.ID_RATING, m.id as mpa_id, m.name as mpa_name, COUNT(fl.user_id) as likes,\n"
                    + "                GROUP_CONCAT( DISTINCT g.genre_id SEPARATOR ',') as genresid, \n"
                    + "                GROUP_CONCAT(gs.name SEPARATOR ',') as genresnames"
                    + "                FROM FILMS as f\n"
                    + "                join mpa as m on m.id = f.id_rating\n"
                    + "                LEFT OUTER join films_users_likes as fl on f.id = fl.film_id\n"
                    + "                left outer join films_genres as g on g.film_id = f.id\n"
                    + "                left outer JOIN genres AS gs ON gs.id = g.GENRE_ID\n"
                    + "                where f.id = ? \n"
                    + "                GROUP BY f.id\n"
                    + "                ORDER BY COUNT(fl.user_id)";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public long create(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, id_rating, rate) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[]{"id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, Date.valueOf(film.getReleaseDate()));
            statement.setLong(4, film.getDuration());
            statement.setLong(5, film.getMpa().getId());
            statement.setLong(6, film.getRate());
            return statement;
        }, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        updateGenresOfFilm(id, film.getGenres());
        return id;
    }

    @Override
    public Optional<Film> update(Film film) {
        String sqlQuery = "update films set " +
                "name = ?, description = ?, release_date = ?, duration = ?, id_rating = ?, rate = ?" +
                "where id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getRate(), film.getId());
        updateGenresOfFilm(film.getId(), film.getGenres());
        return findFilm(film.getId());
    }

    @Override
    public void remove(Film film) {
        String sqlQuery = "delete from films where id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION,\n"
                + "             f.ID_RATING, m.id as mpa_id, m.name as mpa_name, COUNT(fl.user_id) as likes,\n"
                + "             GROUP_CONCAT( DISTINCT g.genre_id SEPARATOR ',') as genresid, \n"
                + "             GROUP_CONCAT(gs.name SEPARATOR ',') as genresnames\n"
                + "             FROM FILMS as f\n"
                + "             join mpa as m on m.id = f.id_rating\n"
                + "             LEFT OUTER join films_users_likes as fl on f.id = fl.film_id\n"
                + "             left outer join films_genres as g on g.film_id = f.id\n"
                + "             left outer join genres AS gs ON gs.id = g.GENRE_ID \n"
                + "             GROUP BY f.id";
        List<Optional<Film>> foundFilms = jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
        List<Film> result = new ArrayList<>();
        for (Optional<Film> filmOptional : foundFilms) {
            filmOptional.ifPresent(result::add);
        }
        return result;
    }

    @Override
    public void addLike(long filmId, long userId) {
        String sqlQuery = "INSERT INTO films_users_likes (film_id, user_id) " +
                "VALUES (?, ?);";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        String sqlQuery = "delete from films_users_likes where userId = ? and filmId = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> getPopular(int count) {
        String sqlQuery = "SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION,"
                + "                f.ID_RATING, m.id as mpa_id, m.name as mpa_name, COUNT(fl.user_id) as likes,\n"
                + "                GROUP_CONCAT( DISTINCT g.genre_id SEPARATOR ',') as genresid, \n"
                + "                GROUP_CONCAT(gs.name SEPARATOR ',') as genresnames"
                + "                FROM FILMS as f\n"
                + "                join mpa as m on m.id = f.id_rating\n"
                + "                LEFT OUTER join films_users_likes as fl on f.id = fl.film_id\n"
                + "                left outer join films_genres as g on g.film_id = f.id\n"
                + "                left outer JOIN genres AS gs ON gs.id = g.GENRE_ID\n"
                + "                GROUP BY f.id\n"
                + "                ORDER BY COUNT(fl.user_id) DESC LIMIT ?";
        List<Optional<Film>> foundFilms = jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
        List<Film> result = new ArrayList<>();
        for (Optional<Film> filmOptional : foundFilms) {
            filmOptional.ifPresent(result::add);
        }
        return result;
    }

    private void updateGenresOfFilm(long id, List<Genre> genres) {
        if (genres != null) {
            String sqlQueryClearGenres = "DELETE from films_genres where film_id=?;";
            jdbcTemplate.update(sqlQueryClearGenres, id);
            List<Genre> unGenres =
                    new ArrayList<>(new LinkedHashSet<>(genres));
            for (Genre genre : unGenres) {
                String sqlQueryAddGenresToFilm = "INSERT INTO FILMS_GENRES\n"
                        + "(FILM_ID, GENRE_ID)\n"
                        + "VALUES(?, ?);\n";
                jdbcTemplate.update(sqlQueryAddGenresToFilm, id, genre.getId());
            }
        } else updateGenresOfFilm(id, List.of());
    }

    private Optional<Film> mapRowToFilm(ResultSet resultSet, int i) throws SQLException {
        Mpa mpa = Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("mpa_name"))
                .build();
        List<Genre> genres = new ArrayList<>();
        String genresIdString = resultSet.getString("genresid");
        String genresNamesString = resultSet.getString("genresnames");

        if (genresIdString != null && genresNamesString != null && !genresIdString.equals("")
                && !genresNamesString.equals("")) {
            String[] genresId = genresIdString.split(",");
            String[] genresNames = genresNamesString.split(",");
            for (int c = 0; c < genresId.length; c++) {
                genres.add(Genre.builder().id(Integer.parseInt(genresId[c])).name(genresNames[c]).build());
            }
        }
        int rate = resultSet.getInt("likes");
        return Optional.of(Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mpa)
                .rate(rate)
                .genres(genres)
                .build());
    }
}
