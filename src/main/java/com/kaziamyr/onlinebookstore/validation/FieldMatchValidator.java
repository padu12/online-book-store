package com.kaziamyr.onlinebookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch,
        Object> {
    private String firstField;
    private String secondField;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstField = constraintAnnotation.firstField();
        secondField = constraintAnnotation.secondField();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        try {
            Object firstFieldValue = object.getClass().getDeclaredField(firstField).get(object);
            Object secondFieldValue = object.getClass().getDeclaredField(secondField).get(object);
            return Objects.equals(firstFieldValue, secondFieldValue);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException("Can't find field with name " + firstField + " of "
                    + secondField);
        }

    }
}
