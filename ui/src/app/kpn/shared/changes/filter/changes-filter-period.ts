// this class is generated, please do not modify

export class ChangesFilterPeriod {

  constructor(public name?: string,
              public totalCount?: number,
              public impactedCount?: number,
              public current?: boolean,
              public selected?: boolean,
              public periods?: Array<ChangesFilterPeriod>) {
  }

  public static fromJSON(jsonObject): ChangesFilterPeriod {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ChangesFilterPeriod();
    instance.name = jsonObject.name;
    instance.totalCount = jsonObject.totalCount;
    instance.impactedCount = jsonObject.impactedCount;
    instance.current = jsonObject.current;
    instance.selected = jsonObject.selected;
    instance.periods = jsonObject.periods ? jsonObject.periods.map(json => ChangesFilterPeriod.fromJSON(json)) : [];
    return instance;
  }
}

