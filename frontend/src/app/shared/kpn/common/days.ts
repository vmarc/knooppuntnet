import { Day } from '@api/custom/day';

export class Days {
  static youngerThan(some: Day, other: Day): boolean {
    return some > other;
  }

  static sameAs(some: Day, other: Day): boolean {
    return some === other;
  }

  static sameAsOrYoungerThan(some: Day, other: Day): boolean {
    return Days.sameAs(some, other) || Days.youngerThan(some, other);
  }

  static olderThan(some: Day, other: Day): boolean {
    return !Days.sameAsOrYoungerThan(some, other);
  }
}
