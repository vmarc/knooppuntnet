// this class is generated, please do not modify

import {List} from 'immutable';

export class ChangesFilterPeriod {
  readonly name: string;
  readonly totalCount: number;
  readonly impactedCount: number;
  readonly current: boolean;
  readonly selected: boolean;
  readonly periods: List<ChangesFilterPeriod>;

  constructor(name: string,
              totalCount: number,
              impactedCount: number,
              current: boolean,
              selected: boolean,
              periods: List<ChangesFilterPeriod>) {
    this.name = name;
    this.totalCount = totalCount;
    this.impactedCount = impactedCount;
    this.current = current;
    this.selected = selected;
    this.periods = periods;
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
