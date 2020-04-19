// this class is generated, please do not modify

export class Day {

  constructor(readonly year: number,
              readonly month: number,
              readonly day: number) {
  }

  public static fromJSON(jsonObject: any): Day {
    if (!jsonObject) {
      return undefined;
    }
    return new Day(
      jsonObject.year,
      jsonObject.month,
      jsonObject.day
    );
  }
}
