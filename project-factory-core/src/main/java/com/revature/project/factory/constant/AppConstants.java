package com.revature.project.factory.constant;

/**
 * All Application wide constants like header information, messages files are placed here
 * 
 */

public class AppConstants {
  private AppConstants() {}

  // Header Constants
  public static final String ENCRYPTED_TOKEN_HEADER = "encryptedToken";
  public static final String EMP_ID_HEADER = "empId";

  // Token validation constants
  public static final String TOKEN_REQUIRED = "Token is required to access this resource";
  public static final String TOKEN_VERIFICATION_FAILED = "Token verification fails";
  public static final String INVALID_TOKEN = "Invalid Token";
  public static final String ACCESS_DENIED_TO_THE_RESOURCE = "Access denied for this application";
  public static final String NUMBERS_ONLY_REGEX = "[0-9]+";

  // Mail Config Constants
  public static final String EMAIL_FROM_NAME = "RevaturePro";
  public static final String APP_CONFIG_MAIL_HOST = "mail.host";
  public static final String APP_CONFIG_MAIL_PORT = "mail.port";
  public static final String APP_CONFIG_MAIL_FROM = "mail.from";
  public static final String APP_CONFIG_MAIL_USERNAME = "mail.username";
  public static final String APP_CONFIG_MAIL_PASWORD = "mail.password";
  public static final String APP_CONFIG_MAIL_SMTP_STARTTLS = "mail.smtp.starttls";
  public static final String APP_CONFIG_MAIL_SMTP_AUTH = "mail.smtp.auth";
  public static final String APP_CONFIG_MAIL_SMTP_QUIT_WAIT = "mail.smtp.quit.wait";
  public static final String APP_CONFIG_MAIL_MAIL_SMTP_SSL = "mail.mail.smtp.ssl";
}
