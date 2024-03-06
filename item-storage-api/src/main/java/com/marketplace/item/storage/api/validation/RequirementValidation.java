package com.marketplace.item.storage.api.validation;

import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.item.storage.api.common.Constants;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RequirementValidation implements ConstraintValidator<FieldRequirementValidation, Object>, Constants {

    private boolean nullable;
    private String name;
    private String message;

    @Override
    public void initialize(FieldRequirementValidation constraintAnnotation) {
        nullable = constraintAnnotation.nullable();
        name = constraintAnnotation.name();
        message = constraintAnnotation.message();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (nullable && value == null) return true;
        if (ObjectUtils.isEmpty(value))
            throw new CustomException(HttpStatus.BAD_REQUEST).setDetails(String.format(message, name));
        return true;
    }
}
