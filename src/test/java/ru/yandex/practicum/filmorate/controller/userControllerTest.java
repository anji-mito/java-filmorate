package ru.yandex.practicum.filmorate.controller;

import org.junit.Assert;
import org.junit.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class userControllerTest {
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    public void emptyFieldsValidationTest() {
        final User user = new User();
        user.setLogin("Login with space");
        Set<ConstraintViolation<User>> validates = validator.validate(user);
        Assert.assertTrue(validates.size() > 0);
        validates.stream().map(ConstraintViolation::getMessage).forEach(System.out::println);
    }
}
