package com.revature.project.factory.service.constant;


public final class BusinessConstants {

  public static final String INVALID_PROJECT_ID = "Invalid project id";
  public static final String PROJECT_NOT_FOUND_FOR_THE_GIVEN_ID =
      "Project not found for the given project id : %d";
  public static final String INVALID_FILTER_OPTIONS = "Invalid filter options";
  public static final String NAME = "name";
  public static final String ID = "id";
  public static final String SCORE = "score";
  public static final String DESC = "desc";
  public static final String ASC = "asc";
  public static final String MAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
      + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
  public static final String MANDATORY_FIELDS_MISSING = "Mandatory fields are missing";
  public static final String INCORRECT_USERNAME_PWD = "Incorrect username or password";
  public static final String INVALID_USER = "Invalid user";
  public static final String USER_DEACTIVATED = "User has been deactivated";
  public static final String WELCOME_TITLE = "Welcome to Revature Pro";
  public static final String SOMETHING_WENT_WRONG = "Something went wrong";
  public static final String TOKEN_VERIFICATION_FAILED = "Token verification fails";

  private BusinessConstants() {}
}
