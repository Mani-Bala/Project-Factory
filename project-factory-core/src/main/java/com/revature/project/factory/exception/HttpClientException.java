package com.revature.project.factory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

public class HttpClientException extends Exception {
  private static final long serialVersionUID = 1L;

  private final String customStatusCode;
  private final HttpStatus statusCode;

  /**
   * Construct a new instance of with the given response data.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *        {@link #getMessage()}
   * @param statusCode the HTTP status code value. The status code is saved for later retrieval by
   *        the {@link #getStatusCode()}
   * @param customStatusCode the Revature custom status code. The custom status code is saved for
   *        later retrieval by the {@link #getStatusCode()}
   */
  HttpClientException(String message, HttpStatus statusCode, String customStatusCode) {
    super(message);
    this.statusCode = statusCode;
    this.customStatusCode = customStatusCode;
  }

  /**
   * Return the raw HTTP status code.
   */
  public HttpStatus getStatusCode() {
    return this.statusCode;
  }

  /**
   * Return the custom status code.
   */
  public String getCustomStatusCode() {
    return this.customStatusCode;
  }


  // Subclasses for specific HTTP status codes

  /**
   * {@link HttpClientException} for status HTTP 401 Unauthorized.
   * 
   */
  public static class Unauthorized extends HttpClientException {

    public Unauthorized(String message, @Nullable String customStatusCode) {
      super(message, HttpStatus.UNAUTHORIZED, customStatusCode);
    }
  }

  /**
   * {@link HttpClientException} for status HTTP 422 Unprocessable Entity.
   *
   */
  public static class UnprocessableEntity extends HttpClientException {

    public UnprocessableEntity(String message, @Nullable String customStatusCode) {
      super(message, HttpStatus.UNPROCESSABLE_ENTITY, customStatusCode);
    }
  }

  /**
   * {@link HttpClientException} for status HTTP 409 Conflict.
   *
   */
  @SuppressWarnings("serial")
  public static class Conflict extends HttpClientException {

    public Conflict(String message, @Nullable String customStatusCode) {
      super(message, HttpStatus.CONFLICT, customStatusCode);
    }
  }
}
