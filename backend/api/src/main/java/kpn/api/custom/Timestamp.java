package kpn.api.custom;

public record Timestamp(
  int year,
  int month,
  int day,
  int hour,
  int minute,
  int second
) implements Comparable<Timestamp> {

  public static final Timestamp redaction = new Timestamp(2012, 9, 12, 6, 55, 0);
  // public static final Timestamp analysisStart = new Timestamp(2019, 11, 1, 0, 0, 0); // 003/739/602
  public static final Timestamp analysisStart = new Timestamp(2025, 12, 15, 0, 0, 0); // 006/900/377 state of the initial server-2 database download

  public Timestamp(int year, int month, int day) {
    this(year, month, day, 0, 0, 0);
  }

  public static Timestamp fromKey(String key) {
    if (key.length() != "yyyyMMddHHmmss".length()) {
      throw new IllegalArgumentException("Invalid key length");
    }
    int year = Integer.parseInt(key.substring(0, 4));
    int month = Integer.parseInt(key.substring(4, 6));
    int day = Integer.parseInt(key.substring(6, 8));
    int hour = Integer.parseInt(key.substring(8, 10));
    int minute = Integer.parseInt(key.substring(10, 12));
    int second = Integer.parseInt(key.substring(12, 14));
    return new Timestamp(year, month, day, hour, minute, second);
  }

  public static Timestamp fromLogKey(String key) {
    if (key.length() != "yyyy-MM-dd HH:mm".length()) {
      throw new IllegalArgumentException("Invalid key length");
    }
    int year = Integer.parseInt(key.substring(0, 4));
    int month = Integer.parseInt(key.substring(5, 7));
    int day = Integer.parseInt(key.substring(8, 10));
    int hour = Integer.parseInt(key.substring(11, 13));
    int minute = Integer.parseInt(key.substring(14, 16));
    return new Timestamp(year, month, day, hour, minute, 0);
  }

  public static Timestamp fromIso(String iso) {
    int year = Integer.parseInt(iso.substring(0, 4));
    int month = Integer.parseInt(iso.substring(5, 7));
    int day = Integer.parseInt(iso.substring(8, 10));
    int hour = Integer.parseInt(iso.substring(11, 13));
    int minute = Integer.parseInt(iso.substring(14, 16));
    int second = Integer.parseInt(iso.substring(17, 19));
    return new Timestamp(year, month, day, hour, minute, second);
  }

  public static Timestamp apply(Day day) {
    int dayOfMonth = day.day().orElse(1);
    return new Timestamp(day.year(), day.month(), dayOfMonth, 0, 0, 0);
  }

  public Day toDay() {
    return new Day(year, month, java.util.Optional.of(day));
  }

  public String key() {
    return year + monthString() + dayString() + hourString() + minuteString() + secondString();
  }

  public String yyyymmdd() {
    return year + "-" + monthString() + "-" + dayString();
  }

  public String hhmmss() {
    return hourString() + ":" + minuteString() + ":" + secondString();
  }

  public String yyyymmddhhmmss() {
    return yyyymmdd() + " " + hhmmss();
  }

  public String yyyymmddhhmm() {
    return yyyymmdd() + " " + hourString() + ":" + minuteString();
  }

  public String yearString() {
    return String.valueOf(year);
  }

  public String monthString() {
    return to2digitString(month);
  }

  public String dayString() {
    return to2digitString(day);
  }

  public String hourString() {
    return to2digitString(hour);
  }

  public String minuteString() {
    return to2digitString(minute);
  }

  public String secondString() {
    return to2digitString(second);
  }

  public String iso() {
    return yyyymmdd() + "T" + hhmmss() + "Z";
  }

  public boolean greaterThan(Timestamp other) {
    if (year > other.year) {
      return true;
    } else if (year < other.year) {
      return false;
    } else if (month > other.month) {
      return true;
    } else if (month < other.month) {
      return false;
    } else if (day > other.day) {
      return true;
    } else if (day < other.day) {
      return false;
    } else if (hour > other.hour) {
      return true;
    } else if (hour < other.hour) {
      return false;
    } else if (minute > other.minute) {
      return true;
    } else if (minute < other.minute) {
      return false;
    } else {
      return second > other.second;
    }
  }

  public boolean lessThan(Timestamp other) {
    if (year < other.year) {
      return true;
    } else if (year > other.year) {
      return false;
    } else if (month < other.month) {
      return true;
    } else if (month > other.month) {
      return false;
    } else if (day < other.day) {
      return true;
    } else if (day > other.day) {
      return false;
    } else if (hour < other.hour) {
      return true;
    } else if (hour > other.hour) {
      return false;
    } else if (minute < other.minute) {
      return true;
    } else if (minute > other.minute) {
      return false;
    } else {
      return second < other.second;
    }
  }

  public boolean greaterThanOrEqual(Timestamp other) {
    return equals(other) || greaterThan(other);
  }

  public int compareTo(Timestamp other) {
    if (greaterThan(other)) {
      return 1;
    } else if (lessThan(other)) {
      return -1;
    } else {
      return 0;
    }
  }

  private String to2digitString(int value) {
    return (value < 10 ? "0" : "") + value;
  }
}
