package com.revature.project.factory.dao.exception;

public class DataRetrievalFailedException extends Exception {

  private static final long serialVersionUID = 1L;

  public DataRetrievalFailedException() {
    super();
  }

  public DataRetrievalFailedException(String message) {
    super(message);
  }

  public DataRetrievalFailedException(Throwable exception) {
    super(exception);
  }

  public DataRetrievalFailedException(String message, Throwable exception) {
    super(message, exception);
  }
}
