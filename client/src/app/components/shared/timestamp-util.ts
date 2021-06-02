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

  // youngerThan(timestamp1: Timestamp, other: Timestamp): boolean {
  //   if (timestamp1.year > other.year) {
  //     return true;
  //   }
  //
  //   if (this.year < other.year) {
  //     return false;
  //   }
  //
  //   if (this.month > other.month) {
  //     return true;
  //   }
  //
  //   if (this.month < other.month) {
  //     return false;
  //   }
  //
  //   if (this.day > other.day) {
  //     return true;
  //   }
  //
  //   if (this.day < other.day) {
  //     return false;
  //   }
  //
  //   if (this.hour > other.hour) {
  //     return true;
  //   }
  //
  //   if (this.hour < other.hour) {
  //     return false;
  //   }
  //
  //   if (this.minute > other.minute) {
  //     return true;
  //   }
  //
  //   if (this.minute < other.minute) {
  //     return false;
  //   }
  //
  //   return this.second > other.second;
  // }
  //
  // sameAs(other: Timestamp): boolean {
  //   return (
  //     this.year === other.year &&
  //     this.month === other.month &&
  //     this.day === other.day &&
  //     this.hour === other.hour &&
  //     this.minute === other.minute &&
  //     this.second === other.second
  //   );
  // }
  //
  // sameAsOrYoungerThan(other: Timestamp): boolean {
  //   return this.sameAs(other) || this.youngerThan(other);
  // }
  //
  // olderThan(other: Timestamp): boolean {
  //   return !this.sameAsOrYoungerThan(other);
  // }
}
