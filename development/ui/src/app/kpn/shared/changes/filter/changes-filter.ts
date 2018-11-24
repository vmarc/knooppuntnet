// this class is generated, please do not modify

import {List} from 'immutable';
import {ChangesFilterPeriod} from './changes-filter-period';

export class ChangesFilter {
  readonly periods: List<ChangesFilterPeriod>;

  constructor(periods: List<ChangesFilterPeriod>) {
    this.periods = periods;
  }

  public static fromJSON(jsonObject): ChangesFilter {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangesFilter(
      jsonObject.periods ? List(jsonObject.periods.map(json => ChangesFilterPeriod.fromJSON(json))) : List()
    );
  }
}
