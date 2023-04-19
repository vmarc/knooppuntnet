import { SurveyDateInfo } from '@api/common';
import { DayPipe } from '@app/components/shared/format';

export class SurveyDateValues {
  constructor(
    readonly lastMonthStart: string,
    readonly lastHalfYearStart: string,
    readonly lastYearStart: string,
    readonly lastTwoYearsStart: string
  ) {}

  static from(surveyDateInfo: SurveyDateInfo): SurveyDateValues {
    const pipe = new DayPipe('en');
    return new SurveyDateValues(
      pipe.transform(surveyDateInfo.lastMonthStart),
      pipe.transform(surveyDateInfo.lastHalfYearStart),
      pipe.transform(surveyDateInfo.lastYearStart),
      pipe.transform(surveyDateInfo.lastTwoYearsStart)
    );
  }
}
