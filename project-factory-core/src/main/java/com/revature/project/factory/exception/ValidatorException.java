package com.revature.project.factory.exception;

public class ValidatorException extends Exception {

  public ValidatorException(String message) {
    super(message);
  }

  public ValidatorException(String message, Throwable cause) {
    super(message, cause);
  }
}
