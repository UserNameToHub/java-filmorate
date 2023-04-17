package ru.yandex.practicum.filmorate.validation.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.validation.annotation.ReleaseDateConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ReleaseDateConstraintValidator implements ConstraintValidator<ReleaseDateConstraint, LocalDate> {
    private String verificationDate;

    @Override
    public void initialize(ReleaseDateConstraint constraintAnnotation) {
        verificationDate = constraintAnnotation.from();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Start validator for release date.");
        return localDate.isAfter(LocalDate.parse(verificationDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }
}
