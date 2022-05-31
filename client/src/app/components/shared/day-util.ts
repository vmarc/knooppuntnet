import { Day } from "@api/custom/day";

export class DayUtil {
  static year(day: Day): string {
    if (day) {
      return day.substring(0, 4);
    }
    return "";
  }

  static month(day: Day): string {
    if (day) {
      return day.substring(5, 7);
    }
    return "";
  }

  static day(day: Day): string {
    if (day) {
      return day.substring(8, 10);
    }
    return "";
  }
}
