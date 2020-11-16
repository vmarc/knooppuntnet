// this class is generated, please do not modify

import {List} from 'immutable';
import {ChangesFilterPeriod} from './changes-filter-period';

export class ChangesFilter {

  constructor(readonly periods: List<ChangesFilterPeriod>) {
  }

  public static fromJSON(jsonObject: any): ChangesFilter {
    if (!jsonObject) {
      return undefined;
    }
    return new ChangesFilter(
      jsonObject.periods ? List(jsonObject.periods.map((json: any) => ChangesFilterPeriod.fromJSON(json))) : List()
    );
  }
}
