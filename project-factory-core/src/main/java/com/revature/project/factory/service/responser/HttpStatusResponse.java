package com.revature.project.factory.service.responser;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Response class for public http requests
 * 
 * @since v2.10
 */
@JsonInclude(Include.NON_NULL)
public class HttpStatusResponse implements Serializable {

  private static final long serialVersionUID = 1L;
  private static HttpStatusResponse httpResponse;

  private Integer totalRecords;
  private Integer statusCode;
  private String description;
  private transient Object data;
  private transient Object error;
  @JsonInclude(Include.NON_EMPTY)
  private String customCode;

  /**
   * Default constructor
   */
  private HttpStatusResponse() {
    super();
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(Integer i) {
    this.statusCode = i;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public Integer getTotalRecords() {
    return totalRecords;
  }

  public void setTotalRecords(Integer totalRecords) {
    this.totalRecords = totalRecords;
  }

  public Object getError() {
    return error;
  }

  public void setError(Object error) {
    this.error = error;
  }

  public String getCustomCode() {
    return customCode;
  }

  public void setCustomCode(String customCode) {
    this.customCode = customCode;
  }

  /**
   * Method returns the response object with total records,code,data and description.
   * 
   * @param code - Status code
   * @param description - Response description
   * @param data - Actual data
   * @return httpResponse
   */
  public static HttpStatusResponse setHttpResponse(Integer code, String description, Object data,
      String customCode) {
    httpResponse = new HttpStatusResponse();
    httpResponse.setTotalRecords(null);
    httpResponse.setStatusCode(code);
    httpResponse.setDescription(description);
    httpResponse.setData(data);
    httpResponse.setCustomCode(customCode);
    return httpResponse;
  }

  /**
   * Method returns the response object with code,error and description.
   *
   * @param code - Status code
   * @param description - Response description
   * @param error - Actual error
   * @return httpResponse
   */
  public static HttpStatusResponse setHttpErrorResponse(Integer code, String description,
      Object error) {
    httpResponse = new HttpStatusResponse();
    httpResponse.setTotalRecords(null);
    httpResponse.setStatusCode(code);
    httpResponse.setDescription(description);
    httpResponse.setError(error);
    return httpResponse;
  }
}
