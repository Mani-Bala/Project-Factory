package com.revature.project.factory.exception;


/**
 * Exception class meant to be thrown for any service primarily to restrict invalid data.
 * 
 */
public class UnprocessableEntityException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * This is for throw the message
   *
   * @param msg
   * @param exception
   */
  public UnprocessableEntityException(String msg, Throwable exception) {
    super(msg, exception);
  }

  /**
   *
   * @param msg
   */
  public UnprocessableEntityException(String msg) {
    super(msg);
  }
}
