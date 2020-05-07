// this class is generated, please do not modify

import {Day} from "../custom/day";

export class SurveyDateTimeInfo {

  constructor(readonly now: Day,
              readonly lastMonthStart: Day,
              readonly lastHalfYearStart: Day,
              readonly lastYearStart: Day,
              readonly lastTwoYearsStart: Day) {
  }

  public static fromJSON(jsonObject: any): SurveyDateTimeInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new SurveyDateTimeInfo(
      Day.fromJSON(jsonObject.now),
      Day.fromJSON(jsonObject.lastMonthStart),
      Day.fromJSON(jsonObject.lastHalfYearStart),
      Day.fromJSON(jsonObject.lastYearStart),
      Day.fromJSON(jsonObject.lastTwoYearsStart)
    );
  }
}
