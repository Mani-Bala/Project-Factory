package com.revature.project.factory.service.exception;

/**
 * Exception class meant to be thrown by service impls. It's meant primarily as a validation.
 * 
 */
public class BusinessConflictException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * This is for throw the message
   */
  public BusinessConflictException(String msg, Throwable exception) {
    super(msg, exception);
  }

}
