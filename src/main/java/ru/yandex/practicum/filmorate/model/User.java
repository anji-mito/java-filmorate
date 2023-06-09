package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validation.annotation.NoSpaces;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Data
@Builder
public class User {
    public Set<Long> friends = new HashSet<>();
    long id;
    @NotNull(message = "Email can not be null")
    @Email(message = "Incorrect format of email")
    String email;
    @NotNull(message = "Login can not be empty")
    @NotBlank(message = "Login can not be empty")
    @NoSpaces(message = "Login can not have spaces")
    String login;
    String name;
    @Past(message = "Birthday can not be in the future")
    LocalDate birthday;

}