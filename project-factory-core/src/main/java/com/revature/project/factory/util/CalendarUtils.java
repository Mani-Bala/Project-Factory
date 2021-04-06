package com.revature.project.factory.util;

import static java.sql.Timestamp.valueOf;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class CalendarUtils {

  private static final String DEFAULT_TIME_ZONE = "UTC";

  private CalendarUtils() {}

  /**
   * Used to get the current date and time in utc timezone
   * 
   * @return present time in utc format.
   */
  public static Timestamp getNowInUTC() {
    return valueOf(LocalDateTime.now(ZoneId.of(DEFAULT_TIME_ZONE)));
  }

  public static LocalDateTime getNowInUTCDateTime() {
    return LocalDateTime.now(ZoneId.of(DEFAULT_TIME_ZONE));
  }

}
