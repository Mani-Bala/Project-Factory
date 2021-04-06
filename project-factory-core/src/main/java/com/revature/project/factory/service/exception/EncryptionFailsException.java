package com.revature.project.factory.service.exception;

/**
 * Exception class meant to be thrown by service. It's meant primarily as a exception for encryption
 * related logic.
 * 
 */
public class EncryptionFailsException extends Exception {

  private static final long serialVersionUID = 1L;

  // ---------------------------------- Constructor
  /**
   * empty throw
   */
  public EncryptionFailsException() {
    super();
  }

  /**
   * This is for throw the message
   * 
   * @param message
   */
  public EncryptionFailsException(String msg, Throwable exception) {
    super(msg, exception);
  }

  /**
   * This is for throw the message with cause
   * 
   * @param message
   * @param cause
   */
  public EncryptionFailsException(Throwable exception) {
    super(exception);
  }

  /**
   * This is for throw the cause
   * 
   * @param cause
   */
  public EncryptionFailsException(String message) {
    super(message);
  }
}
