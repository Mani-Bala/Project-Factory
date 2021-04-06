package com.revature.project.factory.validator;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.revature.project.factory.validator.impl.BooleanCheckValidator;

@Documented
@Constraint(validatedBy = BooleanCheckValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BooleanCheck {

  String message() default "The field under validation must be able to be cast as a boolean. Accepted input are true, false, 1, and 0";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
