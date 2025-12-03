import { SurveyDateInfo } from '@api/common/survey-date-info';
import { DayUtil } from '@app/shared/components/day-util';

export class SurveyDateValues {
  constructor(
    readonly lastMonthStart: string,
    readonly lastHalfYearStart: string,
    readonly lastYearStart: string,
    readonly lastTwoYearsStart: string
  ) {}

  static from(surveyDateInfo: SurveyDateInfo): SurveyDateValues {
    return new SurveyDateValues(
      DayUtil.toString('en', surveyDateInfo.lastMonthStart),
      DayUtil.toString('en', surveyDateInfo.lastHalfYearStart),
      DayUtil.toString('en', surveyDateInfo.lastYearStart),
      DayUtil.toString('en', surveyDateInfo.lastTwoYearsStart)
    );
  }
}
