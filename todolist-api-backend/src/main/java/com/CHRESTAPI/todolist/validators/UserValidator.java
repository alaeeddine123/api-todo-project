package com.CHRESTAPI.todolist.validators;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

public class  UserValidator {
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    //sum fucntion

    private static final Validator validator = factory.getValidator();

    public static void validate(Object object) {
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Invalid object: " + violations);
        }
    }
}