package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class UserControllerTest {
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    @Test
    public void emptyFieldsValidationTest() {
        final User user = new User();
        user.setLogin("Login with spaces");
        Set<ConstraintViolation<User>> validates = validator.validate(user);
        assertTrue(validates.size() > 0);
        validates.stream().map(ConstraintViolation::getMessage).forEach(System.out::println);
    }
}
