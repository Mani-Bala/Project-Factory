package com.revature.project.factory.controller.constant;

public class RestURIConstants {
  private RestURIConstants() {}

  public static final String SECURE_BASE_URL = "/secure";
  public static final String STATUS_INFO_ENDPOINT = "/status/info";
  public static final String LOGIN_ENDPOINT = "/login";
  public static final String FORGOT_PASS_WORD_ENDPOINT = "/forgot-password";
  public static final String RESET_PASSWORD = SECURE_BASE_URL + "/reset-password";
  public static final String GET_PROJECTS = SECURE_BASE_URL + "/projects";
  public static final String PROJECT_BY_ID = SECURE_BASE_URL + "/project/{projectId:[\\d]+}";
  public static final String PROJECT = SECURE_BASE_URL + "/project";
  public static final String METADATA = SECURE_BASE_URL + "/metadata";
}
