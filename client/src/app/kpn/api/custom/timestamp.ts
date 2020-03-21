export class Timestamp {
  constructor(readonly year: number,
              readonly month: number,
              readonly day: number,
              readonly hour: number,
              readonly minute: number,
              readonly second: number) {
  }

  public static fromJSON(jsonObject: any): Timestamp {
    if (!jsonObject) {
      return undefined;
    }
    return new Timestamp(
      +jsonObject.substr(0, 4),
      +jsonObject.substr(5, 2),
      +jsonObject.substr(8, 2),
      +jsonObject.substr(11, 2),
      +jsonObject.substr(14, 2),
      +jsonObject.substr(17, 2)
    );
  }

  /*
    def >(other: Timestamp): Boolean = {
    if (year > other.year) {
      true
    }
    else if (year < other.year) {
      false
    }
    else if (month > other.month) {
      true
    }
    else if (month < other.month) {
      false
    }
    else if (day > other.day) {
      true
    }
    else if (day < other.day) {
      false
    }
    else if (hour > other.hour) {
      true
    }
    else if (hour < other.hour) {
      false
    }
    else if (minute > other.minute) {
      true
    }
    else if (minute < other.minute) {
      false
    }
    else {
      second > other.second
    }
  }

   */
  youngerThan(other: Timestamp): boolean {
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

    if (this.day > other.day) {
      return true;
    }

    if (this.day < other.day) {
      return false;
    }

    if (this.hour > other.hour) {
      return true;
    }

    if (this.hour < other.hour) {
      return false;
    }

    if (this.minute > other.minute) {
      return true;
    }

    if (this.minute < other.minute) {
      return false;
    }

    return this.second > other.second;
  }

  sameAs(other: Timestamp): boolean {
    return this.year === other.year &&
      this.month === other.month &&
      this.day === other.day &&
      this.hour === other.hour &&
      this.minute === other.minute &&
      this.second === other.second;
  }

  sameAsOrYoungerThan(other: Timestamp): boolean {
    return this.sameAs(other) || this.youngerThan(other);
  }

  olderThan(other: Timestamp): boolean {
    return !this.sameAsOrYoungerThan(other);
  }

}
