package ru.yandex.practicum.filmorate.validation.annotation;

import javax.validation.Payload;

public @interface NoSpaces {
    String message() default "{spaces are not allowed for login}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
