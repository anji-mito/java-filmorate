package ru.yandex.practicum.filmorate.validation.validator;

import ru.yandex.practicum.filmorate.validation.annotation.NoSpaces;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NoSpacesValidator implements ConstraintValidator<NoSpaces, String> {
    @Override
    public boolean isValid(String name, ConstraintValidatorContext constraintValidatorContext) {
        return !name.contains(" ");
    }
}
