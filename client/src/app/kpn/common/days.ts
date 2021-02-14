import {Day} from '@api/custom/day';

export class Days {

  static youngerThan(some: Day, other: Day): boolean {

    if (some.year > other.year) {
      return true;
    }

    if (some.year < other.year) {
      return false;
    }

    if (some.month > other.month) {
      return true;
    }

    if (some.month < other.month) {
      return false;
    }

    if (some.day === null && other.day === null) {
      return false;
    }

    if (some.day === null && other.day !== null) {
      return false;
    }

    if (some.day !== null && other.day === null) {
      return true;
    }

    return some.day > other.day;
  }

  static sameAs(some: Day, other: Day): boolean {
    return some.year === other.year &&
      some.month === other.month &&
      some.day === other.day;
  }

  static sameAsOrYoungerThan(some: Day, other: Day): boolean {
    return some.sameAs(other) || some.youngerThan(other);
  }

  static olderThan(some: Day, other: Day): boolean {
    return !some.sameAsOrYoungerThan(other);
  }

}
