import { Day } from '@api/custom/day';

export class DayUtil {
  static year(day: Day): string {
    if (day) {
      return day.substr(0, 4);
    }
    return '';
  }

  static month(day: Day): string {
    if (day) {
      return day.substr(5, 2);
    }
    return '';
  }

  static day(day: Day): string {
    if (day) {
      return day.substr(8, 2);
    }
    return '';
  }
}
