package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validation.annotation.ReleaseDate;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Data
@Builder
public class Film {
    long id;
    @NotNull(message = "Name can not be null")
    @NotBlank(message = "Name can not be empty")
    String name;
    @Size(max = 200)
    @NotBlank(message = "Description can not be empty")
    @NotNull(message = "Description can not be null")
    String description;
    @NotNull(message = "Release Date can not be null")
    @ReleaseDate(message = "released date of movie can not be before 1985-12-28 (The cinema Birthday)")
    LocalDate releaseDate;
    @NotNull(message = "Duration can not be null")
    @Min(value = 0, message = "Duration can not be negative")
    Integer duration;
    Mpa mpa;
    int rate;
    List<Genre> genres = new ArrayList<>();

}
