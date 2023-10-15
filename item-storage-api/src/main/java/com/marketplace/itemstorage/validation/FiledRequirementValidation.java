package com.marketplace.itemstorage.validation;

import org.apache.logging.log4j.util.Strings;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.marketplace.itemstorage.common.MessageConstants.FIELD_REQUEST_ERROR;

@Constraint(validatedBy = RequirementValidation.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FiledRequirementValidation {

    String message() default FIELD_REQUEST_ERROR;
    boolean nullable() default false;
    String name() default Strings.EMPTY;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
