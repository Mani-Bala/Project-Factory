package com.revature.project.factory.service.exception;

/**
 * Exception class meant to be thrown primarily as a exception for access related logic.The
 * controller advice is configured to return unauthorized response in case this exception is thrown
 *
 */
public class UnauthorizedException extends Exception {

  private static final long serialVersionUID = 1L;

  private String customCode;

  private Object data;

  // ---------------------------------- Constructor
  /**
   * empty throw
   */
  public UnauthorizedException() {
    super();
  }

  /**
   * This is for throw the message
   *
   * @param message
   */
  public UnauthorizedException(String msg, Throwable exception) {
    super(msg, exception);
  }

  /**
   * This is for throw the message
   *
   * @param msg
   * @param exception
   * @param customCode
   * @param data
   */
  public UnauthorizedException(String msg, Throwable exception, String customCode, Object data) {
    super(msg, exception);
    this.customCode = customCode;
    this.data = data;
  }

  /**
   * This is for throw the message with cause
   *
   * @param message
   * @param cause
   */
  public UnauthorizedException(Throwable exception) {
    super(exception);
  }

  /**
   * This is for throw the cause
   *
   * @param cause
   */
  public UnauthorizedException(String message) {
    super(message);
  }

  public UnauthorizedException(String message, String customCode) {
    super(message);
    this.customCode = customCode;
  }

  public UnauthorizedException(String message, String customCode, Object data) {
    super(message);
    this.customCode = customCode;
    this.data = data;
  }

  public String getCustomCode() {
    return customCode;
  }

  public void setCustomCode(String customCode) {
    this.customCode = customCode;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }
}
