package com.appointmentsched.scheduler;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.time.LocalDateTime;

/**
 * Class DateUtils contains simple utility functions for parsing
 * and formatting dates.
 *
 * @author Jeffrey Bour
 */
public final class DateUtils {

  private static DateTimeFormatter printFormatter = 
      DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT);

  private static DateTimeFormatter parseFormatter =
      DateTimeFormatter.ofPattern("yyyy-M-d h:m a");

  /**
   * Produce a nicely-formatted string representation of a LocalDateTime object.
   * @param dateTime LocalDateTime object.
   * @return string representation of dateTime.
   */
  public static String format(LocalDateTime dateTime) {
    return printFormatter.format(dateTime);
  }

  /**
   * Parse a string to a LocalDateTime object.
   * Note here that days in the range 1-31 are always accepted:
   * default to last day of month if date does not exist.
   * @param dateTimeString date time string.
   * @return LocalDateTime object.
   * @throws DateTimeParseException if dateTimeString is not formatted
   * according to DateTimeFormatter's specifications.
   */
  public static LocalDateTime parse(String dateTimeString)
      throws DateTimeParseException {
    return LocalDateTime.parse(dateTimeString, parseFormatter);
  }

}