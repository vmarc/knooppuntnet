import { Day } from '@api/custom';
import { Timestamp } from '@api/custom';

export class TimestampUtil {
  static day(timestamp: Timestamp): string {
    return timestamp.substring(0, '2020-11-08'.length);
  }

  static formatted(timestamp: Timestamp): string {
    if (timestamp) {
      return timestamp
        .substring(0, '2020-11-08 12:34'.length)
        .replace('T', ' ');
    }
    return '';
  }

  static year(timestamp: Timestamp): string {
    if (timestamp) {
      return timestamp.substring(0, 4);
    }
    return '';
  }

  static month(timestamp: Timestamp): string {
    if (timestamp) {
      return timestamp.substring(5, 7);
    }
    return '';
  }

  static dayPart(timestamp: Timestamp): string {
    if (timestamp) {
      return timestamp.substring(8, 10);
    }
    return '';
  }

  static hour(timestamp: Timestamp): string {
    if (timestamp) {
      return timestamp.substring(11, 13);
    }
    return '';
  }

  static minute(timestamp: Timestamp): string {
    if (timestamp) {
      return timestamp.substring(14, 16);
    }
    return '';
  }

  static second(timestamp: Timestamp): string {
    if (timestamp) {
      return timestamp.substring(17, 19);
    }
    return '';
  }

  static toTimestamp(date: Date): Day {
    if (date === null) {
      return null;
    }
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const dayPart = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${dayPart}T00:00:00Z`;
  }
}
