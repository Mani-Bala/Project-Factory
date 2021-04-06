package com.revature.project.factory.data.access.exception;

public class ReferentialIntegrityException extends DataSourceOperationFailedException {

  private static final long serialVersionUID = 1L;

  public ReferentialIntegrityException(String message, Throwable exception) {
    super(message, exception);
  }
}
