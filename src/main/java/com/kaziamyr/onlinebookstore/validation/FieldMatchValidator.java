package com.kaziamyr.onlinebookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import org.springframework.util.StringUtils;

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
            Object firstFieldValue =
                    object.getClass()
                            .getMethod("get" + StringUtils.capitalize(firstField))
                            .invoke(object);
            Object secondFieldValue =
                    object.getClass()
                            .getMethod("get" + StringUtils.capitalize(firstField))
                            .invoke(object);
            return Objects.equals(firstFieldValue, secondFieldValue);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Can't---- find field with name " + firstField + " of "
                    + secondField);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }
}
