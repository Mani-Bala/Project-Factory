package com.revature.project.factory.service.exception;

/**
 * Exception class meant to be thrown primarily as a exception for authentication related logic.The
 * controller advice is configured to return unauthorized response in case this exception is thrown
 * 
 */
public class AuthenticationFailsException extends Exception {

  private static final long serialVersionUID = 1L;

  // ---------------------------------- Constructor
  /**
   * empty throw
   */
  public AuthenticationFailsException() {
    super();
  }

  /**
   * This is for throw the message
   * 
   * @param message
   */
  public AuthenticationFailsException(String msg, Throwable exception) {
    super(msg, exception);
  }

  /**
   * This is for throw the message with cause
   * 
   * @param message
   * @param cause
   */
  public AuthenticationFailsException(Throwable exception) {
    super(exception);
  }

  /**
   * This is for throw the cause
   * 
   * @param cause
   */
  public AuthenticationFailsException(String message) {
    super(message);
  }
}
