import { Day } from '@api/custom/day';

export class DayUtil {
  static year(day: Day): string {
    if (day) {
      return day.substring(0, 4);
    }
    return '';
  }

  static month(day: Day): string {
    if (day) {
      return day.substring(5, 7);
    }
    return '';
  }

  static day(day: Day): string {
    if (day) {
      return day.substring(8, 10);
    }
    return '';
  }

  static toString(locale: string, day: Day): string {
    if (day) {
      const year = this.year(day);
      const month = this.month(day);
      if (day.length > '2021-08'.length) {
        const dayPart = this.day(day);
        if (locale === 'nl') {
          return `${dayPart}-${month}-${year}`;
        }
        if (locale === 'de') {
          return `${dayPart}.${month}.${year}`;
        }
        if (locale === 'fr') {
          return `${dayPart}/${month}/${year}`;
        }
        return `${year}-${month}-${dayPart}`;
      }
      if (locale === 'nl') {
        return `${month}-${year}`;
      }
      if (locale === 'de') {
        return `${month}.${year}`;
      }
      if (locale === 'fr') {
        return `${month}/${year}`;
      }
      return `${year}-${month}`;
    }
    return null;
  }

  static toDate(day: Day): Date {
    if (day) {
      const year = this.year(day);
      const month = this.month(day);
      const dayPart = this.day(day);
      return new Date(+year, +month, +dayPart);
    }
    return null;
  }

  static toDay(date: Date): Day {
    if (date === null) {
      return null;
    }
    return date.toISOString().substring(0, 'YYYY-MM-DD'.length);
  }

  static now(): Day {
    return this.toDay(new Date());
  }

  static formatString(locale: string): string {
    if (locale === 'nl') {
      return 'DD-MM-YYYY';
    }
    if (locale === 'de') {
      return 'DD.MM.YYYY';
    }
    if (locale === 'fr') {
      return 'DD/MM/YYYY';
    }
    return 'DD-MM-YYYY';
  }

  static localeString(locale: string): string {
    if (locale === 'nl') {
      return 'nl';
    }
    if (locale === 'de') {
      return 'de';
    }
    if (locale === 'fr') {
      return 'fr';
    }
    return 'nl';
  }
}
