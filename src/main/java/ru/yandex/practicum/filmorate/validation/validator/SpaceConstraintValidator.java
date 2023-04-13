package ru.yandex.practicum.filmorate.validation.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validation.annotation.SpaceConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class SpaceConstraintValidator implements ConstraintValidator<SpaceConstraint, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Start validator for space constraint.");
        return !s.contains(" ");
    }
}
