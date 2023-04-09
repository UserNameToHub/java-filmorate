package ru.yandex.practicum.filmorate.validate.annotation;

import ru.yandex.practicum.filmorate.validate.validator.SpaceConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SpaceConstraintValidator.class)
public @interface SpaceConstraint {
    String message() default "{value.negative}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
