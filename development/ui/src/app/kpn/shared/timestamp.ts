export class Timestamp {
  readonly year: number;
  readonly month: number;
  readonly day: number;
  readonly hour: number;
  readonly minute: number;
  readonly second: number;

  constructor(year: number,
              month: number,
              day: number,
              hour: number,
              minute: number,
              second: number) {
    this.year = year;
    this.month = month;
    this.day = day;
    this.hour = hour;
    this.minute = minute;
    this.second = second;
  }

  public static fromJSON(jsonObject): Timestamp {
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
}
