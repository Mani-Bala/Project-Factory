package com.revature.project.factory.util;

import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;

import com.revature.project.factory.exception.UnprocessableEntityException;

/**
 * Contains all common validation helper methods. Provides utility methods for validating
 * {@link Object} instances
 * 
 * @author Revature
 *
 * @param <T>
 */
public class ValidationUtil<T> {

  private static final String IS_INVALID = " is invalid";
  private String fieldName;
  private T value;

  /**
   * Constructor to initializes the value to fieldName and value
   * 
   * @param fieldName
   * @param value
   */
  public ValidationUtil(String fieldName, T value) {
    super();
    this.fieldName = fieldName;
    this.value = value;
  }

  /**
   * Initializes the values to fieldName and the value and return {@link ValidationUtil} instance.
   * 
   * @param fieldName
   * @param value
   * @return {@link ValidationUtil}
   */
  public static <T> ValidationUtil<T> validate(String fieldName, T value) {
    return new ValidationUtil<>(fieldName, value);
  }

  /**
   * Validates the value is {@link <code>null</code>} or not. If the value is
   * {@link <code>null</code>} it will throw the {@link UnprocessableEntityException} exception else
   * it will return the same {@link ValidationUtil} instance.
   * 
   * @return {@link ValidationUtil}
   * @throws UnprocessableEntityException
   */
  public ValidationUtil<T> required() throws UnprocessableEntityException {
    if (Objects.isNull(value)) {
      throw new UnprocessableEntityException(fieldName + " is required");
    }
    return this;
  }

  /**
   * Validates the value's minimum length ({@link String}) or minimum value ({@link Long} or
   * {@link Integer}) based on the value's type. If the value is not matched with the min it will
   * throw the {@link UnprocessableEntityException} exception else it will return the same
   * {@link ValidationUtil} instance.
   * 
   * @param min
   * @return {@link ValidationUtil}
   * @throws UnprocessableEntityException
   */
  public ValidationUtil<T> min(Integer min) throws UnprocessableEntityException {
    if (Objects.nonNull(value)) {
      if ((value instanceof Long || value instanceof Integer) && Objects.nonNull(min)
          && Integer.parseInt(value.toString()) < min) {
        throw new UnprocessableEntityException(fieldName + " should be greater than " + min);
      }

      if (value instanceof String && Objects.nonNull(min) && value.toString().length() < min) {
        throw new UnprocessableEntityException(
            fieldName + " should be more than " + min + " characters");
      }
    }
    return this;
  }

  /**
   * Validates the value's maximum length ({@link String}) or maximum value ({@link Long} or
   * {@link Integer}) based on the value's type. If the value is not matched with the max it will
   * throw the {@link UnprocessableEntityException} exception else it will return the same
   * {@link ValidationUtil} instance.
   * 
   * @param max
   * @return {@link ValidationUtil}
   * @throws UnprocessableEntityException
   */
  public ValidationUtil<T> max(Integer max) throws UnprocessableEntityException {
    if (Objects.nonNull(value)) {
      if ((value instanceof Long || value instanceof Integer) && Objects.nonNull(max)
          && Integer.parseInt(value.toString()) > max) {
        throw new UnprocessableEntityException(fieldName + " should be lesser than " + max);
      }

      if (value instanceof String && Objects.nonNull(max) && value.toString().length() > max) {
        throw new UnprocessableEntityException(
            fieldName + " should be lesser than " + max + " characters");
      }
    }
    return this;
  }

  /**
   * Validates the value as not-empty value. If the value is empty it will throw the
   * {@link UnprocessableEntityException} exception else it will return the same
   * {@link ValidationUtil} instance.
   * 
   * @return {@link ValidationUtil}
   * @throws UnprocessableEntityException
   */
  public ValidationUtil<T> nonEmpty() throws UnprocessableEntityException {
    if (Objects.nonNull(value) && value.toString().trim().isEmpty()) {
      throw new UnprocessableEntityException(fieldName + IS_INVALID);
    }
    return this;
  }

  /**
   * Validates the value with custom patterns. If the value is not matched with the patterns it will
   * throw the {@link UnprocessableEntityException} exception else it will return the same
   * {@link ValidationUtil} instance.
   * 
   * @param patterns
   * @return {@link ValidationUtil}
   * @throws UnprocessableEntityException
   */
  public ValidationUtil<T> matches(String... patterns) throws UnprocessableEntityException {
    if (Objects.nonNull(value) && Objects.nonNull(patterns)) {
      for (String pattern : patterns) {
        if (!CommonUtils.patternValidator(value.toString(), pattern)) {
          throw new UnprocessableEntityException(fieldName + IS_INVALID);
        }
      }
    }
    return this;
  }

  /**
   * Validates the value as timezone value. If the value is invalid it will throw the
   * {@link UnprocessableEntityException} exception else it will return the same
   * {@link ValidationUtil} instance.
   * 
   * @return {@link ValidationUtil}
   * @throws UnprocessableEntityException
   */
  public ValidationUtil<T> validateTimeZone() throws UnprocessableEntityException {
    if (Objects.nonNull(value) && !Arrays.asList(TimeZone.getAvailableIDs()).contains(value)) {
      throw new UnprocessableEntityException(fieldName + IS_INVALID);
    }
    return this;
  }

  /**
   * Validates the value as month.This method is case insensitive, If the month is invalid it will
   * throw the {@link UnprocessableEntityException} exception else it will return the same
   * {@link ValidationUtil} instance.
   * 
   * @return {@link ValidationUtil}
   * @throws UnprocessableEntityException
   */
  public ValidationUtil<T> validateMonth() throws UnprocessableEntityException {
    List<String> monthNames =
        Arrays.asList(Month.values()).stream().map(Month::toString).collect(Collectors.toList());
    boolean isPresent =
        monthNames.stream().anyMatch(month -> month.equalsIgnoreCase(value.toString()));
    if (Objects.nonNull(value) && !isPresent) {
      throw new UnprocessableEntityException(fieldName + IS_INVALID);
    }
    return this;
  }
}
