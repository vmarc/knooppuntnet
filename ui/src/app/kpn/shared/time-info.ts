// this class is generated, please do not modify

import {Timestamp} from './timestamp';

export class TimeInfo {

  constructor(public now?: Timestamp,
              public lastWeekStart?: Timestamp,
              public lastMonthStart?: Timestamp,
              public lastYearStart?: Timestamp) {
  }

  public static fromJSON(jsonObject): TimeInfo {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new TimeInfo();
    instance.now = jsonObject.now;
    instance.lastWeekStart = jsonObject.lastWeekStart;
    instance.lastMonthStart = jsonObject.lastMonthStart;
    instance.lastYearStart = jsonObject.lastYearStart;
    return instance;
  }
}

