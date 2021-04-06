package com.revature.project.factory.validator.impl;


import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.revature.project.factory.validator.BooleanCheck;

public class BooleanCheckValidator implements ConstraintValidator<BooleanCheck, Object> {

  @Override
  public void initialize(BooleanCheck booleanValue) {
    // empty body
  }

  @Override
  public boolean isValid(Object booleanValue, ConstraintValidatorContext cxt) {
    return Objects.nonNull(booleanValue) && ("1".equalsIgnoreCase(booleanValue.toString())
        || "0".equalsIgnoreCase(booleanValue.toString())
        || "false".equalsIgnoreCase(booleanValue.toString())
        || "true".equalsIgnoreCase(booleanValue.toString()));
  }

}
