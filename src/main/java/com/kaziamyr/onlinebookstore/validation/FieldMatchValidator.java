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
        Object firstFieldValue = getFieldValue(object, firstField);
        Object secondFieldValue = getFieldValue(object, secondField);
        return Objects.equals(firstFieldValue, secondFieldValue);
    }

    private Object getFieldValue(Object object, String field) {
        try {
            return object.getClass()
                    .getMethod("get" + StringUtils.capitalize(field))
                    .invoke(object);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("Can't find field with name " + field);
        }
    }
}
