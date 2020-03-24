// this class is generated, please do not modify

export class PeriodParameters {

  constructor(readonly period: string,
              readonly year: number,
              readonly month: number,
              readonly week: number,
              readonly day: number,
              readonly hour: number) {
  }

  public static fromJSON(jsonObject: any): PeriodParameters {
    if (!jsonObject) {
      return undefined;
    }
    return new PeriodParameters(
      jsonObject.period,
      jsonObject.year,
      jsonObject.month,
      jsonObject.week,
      jsonObject.day,
      jsonObject.hour
    );
  }
}
