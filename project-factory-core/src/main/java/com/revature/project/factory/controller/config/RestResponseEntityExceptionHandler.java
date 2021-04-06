package com.revature.project.factory.controller.config;

import java.io.IOException;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.project.factory.exception.HttpClientException;
import com.revature.project.factory.service.exception.BusinessConflictException;
import com.revature.project.factory.service.exception.BusinessServiceException;
import com.revature.project.factory.service.responser.HttpStatusResponse;
import com.revature.project.factory.util.ResponseUtils;

/**
 * <p>
 * Name: RestResponseEntityExceptionHandler<br>
 * Description: Which is used to handle the thrown exceptions from controllers
 * </p>
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(HttpClientException.class)
  public ResponseEntity<HttpStatusResponse> httpClientException(HttpClientException ex) {
    return ResponseUtils.prepareResponse(ex.getStatusCode(), ex.getMessage(),
        ex.getCustomStatusCode());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<HttpStatusResponse> handleException(Exception ex) {
    return ResponseUtils.prepareInternalServerErrorResponse(ex.getMessage());
  }

  @ExceptionHandler(BusinessServiceException.class)
  public ResponseEntity<HttpStatusResponse> handleBusinessException(BusinessServiceException ex) {
    return ResponseUtils.prepareInternalServerErrorResponse(ex.getMessage());
  }

  @ExceptionHandler(BusinessConflictException.class)
  public ResponseEntity<HttpStatusResponse> handleBusinessConflictException(
      BusinessConflictException ex) {
    return ResponseUtils.prepareConflictResponse(ex.getMessage(), null);
  }

  /**
   * Method to find and display missing parameter in Rest API.
   */
  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    JSONObject jsonObject = new JSONObject();
    ObjectMapper mapper = new ObjectMapper();
    Object error = new Object();
    jsonObject.put("Parameter Name", ex.getParameterName());
    jsonObject.put("Parameter Type", ex.getParameterType());
    try {
      error = mapper.readValue(jsonObject.toString(), Object.class);
    } catch (IOException e) {
      // exception occurs
    }
    return new ResponseEntity<>(HttpStatusResponse.setHttpErrorResponse(status.value(),
        "Required parameter is not present", error), status);
  }

  /**
   * Method to find and display HTTP method and Request URL which is not available.
   */
  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    JSONObject jsonObject = new JSONObject();
    ObjectMapper mapper = new ObjectMapper();
    Object error = new Object();
    jsonObject.put("HTTP method", ex.getHttpMethod());
    jsonObject.put("Request URL", ex.getRequestURL());
    try {
      error = mapper.readValue(jsonObject.toString(), Object.class);
    } catch (IOException e) {
      // exception occurs
    }
    return new ResponseEntity<>(
        HttpStatusResponse.setHttpErrorResponse(status.value(), "No HTTP handler found", error),
        status);
  }

  /**
   * Method to find and display unsupported/supported HTTP method.
   */
  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    JSONObject jsonObject = new JSONObject();
    ObjectMapper mapper = new ObjectMapper();
    Object error = new Object();
    Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
    jsonObject.put("Unsupported HTTP method", ex.getMethod());
    jsonObject.put("Supported HTTP method", supportedMethods);
    try {
      error = mapper.readValue(jsonObject.toString(), Object.class);
    } catch (IOException e) {
      // exception occurs
    }
    return new ResponseEntity<>(
        HttpStatusResponse.setHttpErrorResponse(status.value(), "Invalid HTTP method", error),
        status);
  }

  /**
   * Method to validate entity class required data.
   */
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    JSONObject jsonObject = new JSONObject();
    ObjectMapper mapper = new ObjectMapper();
    Object error = new Object();
    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      jsonObject.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
    try {
      error = mapper.readValue(jsonObject.toString(), Object.class);
    } catch (IOException e) {
      // exception occurs
    }
    return new ResponseEntity<>(
        HttpStatusResponse.setHttpErrorResponse(status.value(), "Validation failed", error),
        status);
  }

  /**
   * Method to find and display unsupported/supported content type.
   */
  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status,
      WebRequest request) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("Unsupported content type", ex.getContentType());
    jsonObject.put("Supported content types", MediaType.toString(ex.getSupportedMediaTypes()));
    ObjectMapper mapper = new ObjectMapper();
    Object error = new Object();
    try {
      error = mapper.readValue(jsonObject.toString(), Object.class);
    } catch (IOException e) {
      // exception occurs
    }
    return new ResponseEntity<>(
        HttpStatusResponse.setHttpErrorResponse(status.value(), "Invalid content type", error),
        status);
  }

  /**
   * Method to find required request body is missing.
   */
  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("message", "Wrong body or no body in request");
    ObjectMapper mapper = new ObjectMapper();
    Object error = new Object();
    try {
      error = mapper.readValue(jsonObject.toString(), Object.class);
    } catch (IOException e) {
      // exception occurs
    }
    return new ResponseEntity<>(HttpStatusResponse.setHttpErrorResponse(status.value(),
        "Required request body is missing", error), status);
  }

  /**
   * Method to configure the Spring DataBinder to use direct field access.
   */
  @InitBinder
  private void activateDirectFieldAccess(DataBinder dataBinder) {
    dataBinder.initDirectFieldAccess();
  }
}
