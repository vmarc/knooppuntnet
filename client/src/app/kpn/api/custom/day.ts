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
    return new Day(jsonObject.year, jsonObject.month, jsonObject.day);
  }
}
