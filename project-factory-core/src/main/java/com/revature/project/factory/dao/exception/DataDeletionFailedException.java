package com.revature.project.factory.dao.exception;

public class DataDeletionFailedException extends Exception {

  private static final long serialVersionUID = 1L;

  public DataDeletionFailedException(String message, Throwable exception) {
    super(message, exception);
  }
}
