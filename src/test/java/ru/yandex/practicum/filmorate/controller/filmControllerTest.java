package ru.yandex.practicum.filmorate.controller;

import org.junit.Assert;
import org.junit.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

public class filmControllerTest {
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    public void emptyFieldsValidationTest() {
        final Film film = new Film();
        film.setReleaseDate(LocalDate.MIN);
        Set<ConstraintViolation<Film>> validates = validator.validate(film);
        Assert.assertTrue(validates.size() > 0);
        validates.stream().map(ConstraintViolation::getMessage).forEach(System.out::println);
    }
}
