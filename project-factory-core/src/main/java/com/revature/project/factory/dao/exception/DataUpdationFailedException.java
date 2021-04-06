package com.revature.project.factory.dao.exception;

public class DataUpdationFailedException extends Exception {

  private static final long serialVersionUID = 1L;

  public DataUpdationFailedException() {
    super();
  }

  public DataUpdationFailedException(String msg, Throwable exception) {
    super(msg, exception);
  }

  public DataUpdationFailedException(Throwable exception) {
    super(exception);
  }

  public DataUpdationFailedException(String message) {
    super(message);
  }

}
