package ru.yandex.practicum.filmorate.validate.annotation;

import ru.yandex.practicum.filmorate.validate.validator.ReleaseDateConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseDateConstraintValidator.class)
public @interface ReleaseDateConstraint {
    String value();

    String message() default "{value.negative}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
