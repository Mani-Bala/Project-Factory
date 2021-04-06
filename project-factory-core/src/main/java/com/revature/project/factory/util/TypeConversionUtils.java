package com.revature.project.factory.util;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public class TypeConversionUtils {

  private TypeConversionUtils() {}

  /**
   * Method converts Any object to Long
   * 
   * @param value - contain any object
   * @return Long converted value
   */
  public static Long toLong(Object value) {
    return value != null ? Long.parseLong(value.toString()) : null;
  }

  /**
   * Method converts Any object to Boolean
   * 
   * @param value - contain any object
   * @return Boolean converted value
   */
  public static Boolean toBool(Object value) {
    Boolean isBool = null;
    if (Objects.nonNull(value)
        && ("1".equalsIgnoreCase(value.toString()) || "true".equalsIgnoreCase(value.toString()))) {
      isBool = true;
    } else if (Objects.nonNull(value)
        && ("0".equalsIgnoreCase(value.toString()) || "false".equalsIgnoreCase(value.toString()))) {
      isBool = false;
    }
    return isBool;
  }

  /**
   * Method converts Timestamp to LocalDateTime
   *
   * @param dateTime - contain any object
   * @return LocalDateTime converted value
   */
  public static LocalDateTime toLocalDateTime(Object dateTime) {
    if (Objects.nonNull(dateTime) && dateTime instanceof Timestamp) {
      Timestamp timestamp = (Timestamp) dateTime;
      return timestamp.toLocalDateTime();
    } else if (Objects.nonNull(dateTime) && dateTime instanceof LocalDateTime) {
      return (LocalDateTime) dateTime;
    } else if (Objects.nonNull(dateTime) && dateTime instanceof String) {
      return LocalDateTime.parse((String) dateTime, DateTimeFormatter.ISO_DATE_TIME);
    }
    return null;
  }

  /**
   * Method to do base 64 decryption of string
   *
   * @param value
   * @return decoded String value
   */
  public static String basic64Decryption(String value) {
    return StringUtils.isNotBlank(value.trim()) ? new String(Base64.getDecoder().decode(value))
        : null;
  }

  /**
   * Method converts Any object to Integer
   *
   * @param value
   * @return Integer converted value
   */
  public static Integer toInteger(Object value) {
    return value != null ? Integer.parseInt(value.toString()) : null;
  }

  /**
   * Method converts Any object to Big Integer
   *
   * @param value
   * @return BigInteger converted value
   */
  public static BigInteger toBigInteger(Object value) {
    return value != null ? BigInteger.valueOf(Long.parseLong(value.toString())) : null;
  }

}
