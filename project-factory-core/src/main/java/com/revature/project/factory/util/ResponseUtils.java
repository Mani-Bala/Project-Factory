package com.revature.project.factory.util;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.google.gson.Gson;
import com.revature.project.factory.service.responser.HttpStatusResponse;

public class ResponseUtils {

  private ResponseUtils() {}

  public static ResponseEntity<HttpStatusResponse> prepareSuccessResponse(String responseMessage,
      Object data, String customCode) {
    if (Objects.isNull(responseMessage)) {
      responseMessage = "Request success";
    }
    return new ResponseEntity<>(HttpStatusResponse.setHttpResponse(HttpStatus.OK.value(),
        responseMessage, data, customCode), HttpStatus.OK);
  }

  public static ResponseEntity<HttpStatusResponse> prepareNoRecordsFoundResponse(
      String responseMessage) {
    if (Objects.isNull(responseMessage)) {
      responseMessage = "No record(s) found";
    }
    return new ResponseEntity<>(
        HttpStatusResponse.setHttpResponse(HttpStatus.OK.value(), responseMessage, null, null),
        HttpStatus.OK);
  }

  public static ResponseEntity<HttpStatusResponse> prepareConflictResponse(String responseMessage,
      String customCode) {
    if (Objects.isNull(responseMessage)) {
      responseMessage = "Some preconditions failed";
    }
    return new ResponseEntity<>(HttpStatusResponse.setHttpResponse(HttpStatus.CONFLICT.value(),
        responseMessage, null, customCode), HttpStatus.CONFLICT);
  }

  public static ResponseEntity<HttpStatusResponse> prepareInternalServerErrorResponse(
      String responseMessage) {
    if (Objects.isNull(responseMessage)) {
      responseMessage = "Something went wrong";
    }
    return new ResponseEntity<>(HttpStatusResponse
        .setHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseMessage, null, null),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public static ResponseEntity<HttpStatusResponse> prepareResponse(HttpStatus httpStatus,
      String responseMessage, String customCode) {
    if (Objects.isNull(responseMessage)) {
      responseMessage = "Something went wrong";
    }
    return new ResponseEntity<>(
        HttpStatusResponse.setHttpResponse(httpStatus.value(), responseMessage, null, customCode),
        httpStatus);
  }

  public static ResponseEntity<HttpStatusResponse> prepareUnAuthorizedResponse(
      String responseMessage) {
    return prepareUnAuthorizedResponse(responseMessage, null);
  }

  public static ResponseEntity<HttpStatusResponse> prepareUnAuthorizedResponse(
      String responseMessage, String customCode) {
    if (Objects.isNull(responseMessage)) {
      responseMessage = "Unauthorized Request";
    }
    return new ResponseEntity<>(HttpStatusResponse.setHttpResponse(HttpStatus.UNAUTHORIZED.value(),
        responseMessage, null, customCode), HttpStatus.UNAUTHORIZED);
  }

  /**
   * Assister method that converts a {@link ServiceResponse} to JSON and writes to the
   * HttpServletResponse.
   */
  public static void setResponse(HttpServletResponse response, String message, HttpStatus status)
      throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(status.value());
    HttpStatusResponse httpStatusResponse =
        HttpStatusResponse.setHttpResponse(status.value(), message, null, null);
    String res = new Gson().toJson(httpStatusResponse);
    response.getWriter().println(res);
  }
}
