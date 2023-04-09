package ru.yandex.practicum.filmorate.validate.validator;

import ru.yandex.practicum.filmorate.validate.annotation.SpaceConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SpaceConstraintValidator implements ConstraintValidator<SpaceConstraint, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return !s.contains(" ");
    }
}
