export class Day {
  constructor(
    readonly year: number,
    readonly month: number,
    readonly day: number
  ) {}

  public static fromJSON(jsonObject: any): Day {
    if (!jsonObject) {
      return undefined;
    }
    return new Day(jsonObject.year, jsonObject.month, jsonObject.day);
  }

  youngerThan(other: Day): boolean {
    if (this.year > other.year) {
      return true;
    }

    if (this.year < other.year) {
      return false;
    }

    if (this.month > other.month) {
      return true;
    }

    if (this.month < other.month) {
      return false;
    }

    if (this.day === null && other.day === null) {
      return false;
    }

    if (this.day === null && other.day !== null) {
      return false;
    }

    if (this.day !== null && other.day === null) {
      return true;
    }

    return this.day > other.day;
  }

  sameAs(other: Day): boolean {
    return (
      this.year === other.year &&
      this.month === other.month &&
      this.day === other.day
    );
  }

  sameAsOrYoungerThan(other: Day): boolean {
    return this.sameAs(other) || this.youngerThan(other);
  }

  olderThan(other: Day): boolean {
    return !this.sameAsOrYoungerThan(other);
  }
}
