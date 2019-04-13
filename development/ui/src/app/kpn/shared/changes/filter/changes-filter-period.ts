// this class is generated, please do not modify

import {List} from "immutable";

export class ChangesFilterPeriod {

  constructor(readonly name: string,
              readonly totalCount: number,
              readonly impactedCount: number,
              readonly current: boolean,
              readonly selected: boolean,
              readonly periods: List<ChangesFilterPeriod>) {
  }

  public static fromJSON(jsonObject): ChangesFilterPeriod {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangesFilterPeriod(
      jsonObject.name,
      jsonObject.totalCount,
      jsonObject.impactedCount,
      jsonObject.current,
      jsonObject.selected,
      jsonObject.periods ? List(jsonObject.periods.map(json => ChangesFilterPeriod.fromJSON(json))) : List()
    );
  }
}
