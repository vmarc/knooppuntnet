export class Day {
  constructor(
    readonly year: number,
    readonly month: number,
    readonly day: number
  ) {}

  static fromJSON(jsonObject: any): Day {
    if (!jsonObject) {
      return undefined;
    }
    const year = +jsonObject.substr(0, 4);
    const month = +jsonObject.substr(5, 2);
    let day: number;
    if (jsonObject.length > 'YYYY-MM'.length) {
      day = +jsonObject.substr(8, 2);
    }
    return new Day(year, month, day);
  }
}
