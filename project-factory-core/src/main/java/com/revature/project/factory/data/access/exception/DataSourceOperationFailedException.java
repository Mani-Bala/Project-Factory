package com.revature.project.factory.data.access.exception;

public class DataSourceOperationFailedException extends DataAccessException {

  private static final long serialVersionUID = 1L;

  public DataSourceOperationFailedException(String message) {
    super(message);
  }

  public DataSourceOperationFailedException(String message, Throwable exception) {
    super(message, exception);
  }
}
