package com.revature.project.factory.data.access.exception;

public class DuplicateRecordException extends DataSourceOperationFailedException {

  private static final long serialVersionUID = 1L;

  public DuplicateRecordException(String message, Throwable exception) {
    super(message, exception);
  }
}
