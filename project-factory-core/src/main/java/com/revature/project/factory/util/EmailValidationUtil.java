package com.revature.project.factory.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidationUtil {

  private static final String EMAIL_PATTERN =
      "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*";

  private EmailValidationUtil() {}

  /**
   * Email validation format.
   * 
   * @param email email
   * @return boolean.
   */
  public static Boolean isValidEmail(String email) {
    if (email == null || email.trim().length() == 0) {
      return false;
    }

    Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();

  }

}
