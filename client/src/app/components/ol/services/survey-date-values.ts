import {SurveyDateInfo} from "../../../kpn/api/common/survey-date-info";
import {DayPipe} from "../../shared/format/day.pipe";

export class SurveyDateValues {
  constructor(readonly lastMonthStart: string,
              readonly lastHalfYearStart: string,
              readonly lastYearStart: string,
              readonly lastTwoYearsStart: string) {
  }

  static from(surveyDateInfo: SurveyDateInfo): SurveyDateValues {
    const pipe = new DayPipe();
    return new SurveyDateValues(
      pipe.transform(surveyDateInfo.lastMonthStart),
      pipe.transform(surveyDateInfo.lastHalfYearStart),
      pipe.transform(surveyDateInfo.lastYearStart),
      pipe.transform(surveyDateInfo.lastTwoYearsStart)
    );
  }
}
