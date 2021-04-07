// this class is generated, please do not modify

export class ActionTimestamp {
  constructor(
    readonly year: number,
    readonly month: number,
    readonly day: number,
    readonly hour: number,
    readonly minute: number,
    readonly second: number,
    readonly weekYear: number,
    readonly weekWeek: number,
    readonly weekDay: number
  ) {}

  static fromJSON(jsonObject: any): ActionTimestamp {
    if (!jsonObject) {
      return undefined;
    }
    return new ActionTimestamp(
      jsonObject.year,
      jsonObject.month,
      jsonObject.day,
      jsonObject.hour,
      jsonObject.minute,
      jsonObject.second,
      jsonObject.weekYear,
      jsonObject.weekWeek,
      jsonObject.weekDay
    );
  }
}
