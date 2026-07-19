package kpn.api.custom;

import java.util.Optional;

import static kpn.core.Util.isDigits;

public record Day(int year, int month, Optional<Integer> day) {

  public Day(int year, int month, int day) {
    this(year, month, Optional.of(day));
  }

  public Day(int year, int month) {
    this(year, month, Optional.empty());
  }

  public static Optional<Day> fromString(String string) {
    if (string.length() == "yyyy-mm-dd".length()) {
      String yearString = string.substring(0, 4);
      String monthString = string.substring(5, 7);
      String dayString = string.substring(8, 10);
      if (isDigits(yearString) && isDigits(monthString) && isDigits(dayString)) {
        return Optional.of(
          new Day(Integer.parseInt(yearString),
            Integer.parseInt(monthString),
            Optional.of(Integer.parseInt(dayString))
          )
        );
      }
    } else if (string.length() == "yyyy-mm".length()) {
      String yearString = string.substring(0, 4);
      String monthString = string.substring(5, 7);
      if (isDigits(yearString) && isDigits(monthString)) {
        return Optional.of(
          new Day(
            Integer.parseInt(yearString),
            Integer.parseInt(monthString),
            Optional.empty()
          )
        );
      }
    }
    return Optional.empty();
  }

  public String yyyymm() {
    return String.format("%d-%02d", year, month);
  }

  public boolean isBefore(Day other) {
    if (this.year < other.year) {
      return true;
    } else if (this.year > other.year) {
      return false;
    } else {
      return this.month < other.month;
    }
  }

  public String yyyymmdd() {
    if (day.isPresent()) {
      return String.format("%d-%02d-%02d", year, month, day.get());
    } else {
      return String.format("%d-%02d", year, month);
    }
  }
}
