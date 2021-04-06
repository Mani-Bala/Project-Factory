package com.revature.project.factory.data.access.exception;

public class DataAccessException extends Exception {

  private static final long serialVersionUID = 1L;

  public DataAccessException(String msg, Throwable exception) {
    super(msg, exception);
  }

  public DataAccessException(String message) {
    super(message);
  }
}
