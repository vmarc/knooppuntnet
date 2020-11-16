// this class is generated, please do not modify

import {Day} from '../custom/day';

export class SurveyDateInfo {

  constructor(readonly now: Day,
              readonly lastMonthStart: Day,
              readonly lastHalfYearStart: Day,
              readonly lastYearStart: Day,
              readonly lastTwoYearsStart: Day) {
  }

  public static fromJSON(jsonObject: any): SurveyDateInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new SurveyDateInfo(
      Day.fromJSON(jsonObject.now),
      Day.fromJSON(jsonObject.lastMonthStart),
      Day.fromJSON(jsonObject.lastHalfYearStart),
      Day.fromJSON(jsonObject.lastYearStart),
      Day.fromJSON(jsonObject.lastTwoYearsStart)
    );
  }
}
