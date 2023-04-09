package ru.yandex.practicum.filmorate.validate.validator;

import ru.yandex.practicum.filmorate.validate.annotation.ReleaseDateConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReleaseDateConstraintValidator implements ConstraintValidator<ReleaseDateConstraint, LocalDate> {
    private String verificationDate;

    @Override
    public void initialize(ReleaseDateConstraint constraintAnnotation) {
        this.verificationDate = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate.isAfter(LocalDate.parse(verificationDate, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }
}
