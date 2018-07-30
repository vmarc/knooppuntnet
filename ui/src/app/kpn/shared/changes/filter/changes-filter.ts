// this class is generated, please do not modify

import {ChangesFilterPeriod} from './changes-filter-period';

export class ChangesFilter {

  constructor(public periods?: Array<ChangesFilterPeriod>) {
  }

  public static fromJSON(jsonObject): ChangesFilter {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new ChangesFilter();
    instance.periods = jsonObject.periods ? jsonObject.periods.map(json => ChangesFilterPeriod.fromJSON(json)) : [];
    return instance;
  }
}

