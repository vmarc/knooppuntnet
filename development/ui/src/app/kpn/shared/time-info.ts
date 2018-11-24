// this class is generated, please do not modify

import {Timestamp} from './timestamp';

export class TimeInfo {
  readonly now: Timestamp;
  readonly lastWeekStart: Timestamp;
  readonly lastMonthStart: Timestamp;
  readonly lastYearStart: Timestamp;

  constructor(now: Timestamp,
              lastWeekStart: Timestamp,
              lastMonthStart: Timestamp,
              lastYearStart: Timestamp) {
    this.now = now;
    this.lastWeekStart = lastWeekStart;
    this.lastMonthStart = lastMonthStart;
    this.lastYearStart = lastYearStart;
  }

  public static fromJSON(jsonObject): TimeInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new TimeInfo(
      Timestamp.fromJSON(jsonObject.now),
      Timestamp.fromJSON(jsonObject.lastWeekStart),
      Timestamp.fromJSON(jsonObject.lastMonthStart),
      Timestamp.fromJSON(jsonObject.lastYearStart)
    );
  }
}
