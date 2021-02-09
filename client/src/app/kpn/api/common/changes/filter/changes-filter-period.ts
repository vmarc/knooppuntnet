// this class is generated, please do not modify

export class ChangesFilterPeriod {

  constructor(readonly name: string,
              readonly totalCount: number,
              readonly impactedCount: number,
              readonly current: boolean,
              readonly selected: boolean,
              readonly periods: Array<ChangesFilterPeriod>) {
  }

  public static fromJSON(jsonObject: any): ChangesFilterPeriod {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangesFilterPeriod(
      jsonObject.name,
      jsonObject.totalCount,
      jsonObject.impactedCount,
      jsonObject.current,
      jsonObject.selected,
      jsonObject.periods ? Array(jsonObject.periods.map((json: any) => ChangesFilterPeriod.fromJSON(json))) : Array()
    );
  }
}
