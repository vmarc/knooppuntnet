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
    instance.now = Timestamp.fromJSON(jsonObject.now);
    instance.lastWeekStart = Timestamp.fromJSON(jsonObject.lastWeekStart);
    instance.lastMonthStart = Timestamp.fromJSON(jsonObject.lastMonthStart);
    instance.lastYearStart = Timestamp.fromJSON(jsonObject.lastYearStart);
    return instance;
  }
}

