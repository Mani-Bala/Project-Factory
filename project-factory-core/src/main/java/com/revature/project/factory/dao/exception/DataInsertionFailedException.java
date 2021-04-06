package com.revature.project.factory.dao.exception;

public class DataInsertionFailedException extends Exception {

  private static final long serialVersionUID = 1L;

  public DataInsertionFailedException() {
    super();
  }

  public DataInsertionFailedException(String msg, Throwable exception) {
    super(msg, exception);
  }

  public DataInsertionFailedException(Throwable exception) {
    super(exception);
  }

  public DataInsertionFailedException(String message) {
    super(message);
  }

}
