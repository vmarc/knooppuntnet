import { Timestamp } from '@api/custom/timestamp';

export class TimestampUtil {
  static day(timestamp: Timestamp): string {
    return timestamp.substr(0, '2020-11-08'.length);
  }

  static formatted(timestamp: Timestamp): string {
    if (timestamp) {
      return timestamp.substr(0, '2020-11-08 12:34'.length).replace('T', ' ');
    }
    return '';
  }

  static year(timestamp: Timestamp): string {
    if (timestamp) {
      return timestamp.substr(0, 4);
    }
    return '';
  }

  static month(timestamp: Timestamp): string {
    if (timestamp) {
      return timestamp.substr(5, 2);
    }
    return '';
  }

  static dayPart(timestamp: Timestamp): string {
    if (timestamp) {
      return timestamp.substr(8, 2);
    }
    return '';
  }

  static hour(timestamp: Timestamp): string {
    if (timestamp) {
      return timestamp.substr(11, 2);
    }
    return '';
  }

  static minute(timestamp: Timestamp): string {
    if (timestamp) {
      return timestamp.substr(14, 2);
    }
    return '';
  }

  static second(timestamp: Timestamp): string {
    if (timestamp) {
      return timestamp.substr(17, 2);
    }
    return '';
  }
}
