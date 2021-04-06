package com.revature.project.factory.service.exception;

/**
 * <p>
 * Name: BusinessServiceException<br>
 * Description: Exception class meant to be thrown by service impls. It's meant primarily to handle
 * business exception
 * </p>
 */
public class ProjectInsertionFailedException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * This is for throw the message
   *
   * {@param message}
   */

  public ProjectInsertionFailedException(String message, Throwable exception) {
    super(message, exception);
  }
}
