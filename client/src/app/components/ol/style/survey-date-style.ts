import { SurveyDateValues } from '@app/core';
import { Color } from 'ol/color';
import { FeatureLike } from 'ol/Feature';
import { Style } from 'ol/style';
import { surveyOlder } from './main-style-colors';
import { surveyLastTwoYearsStart } from './main-style-colors';
import { surveyLastYearStart } from './main-style-colors';
import { surveyLastMonth } from './main-style-colors';
import { surveyLastHalfYearStart } from './main-style-colors';
import { surveyUnknown } from './main-style-colors';
import { NodeStyle } from './node-style';

export class SurveyDateStyle {
  static surveyColor(
    surveyDateValues: SurveyDateValues,
    feature: FeatureLike
  ): Color {
    let color = surveyUnknown; // survey date unknown
    const survey = feature.get('survey');
    if (survey) {
      if (survey > surveyDateValues.lastMonthStart) {
        color = surveyLastMonth;
      } else if (survey > surveyDateValues.lastHalfYearStart) {
        color = surveyLastHalfYearStart;
      } else if (survey > surveyDateValues.lastYearStart) {
        color = surveyLastYearStart;
      } else if (survey > surveyDateValues.lastTwoYearsStart) {
        color = surveyLastTwoYearsStart;
      } else {
        color = surveyOlder;
      }
    }
    return color;
  }

  static smallNodeStyle(
    surveyDateValues: SurveyDateValues,
    feature: FeatureLike
  ): Style {
    let style = NodeStyle.smallSurveyUnknown; // survey date unknown
    const survey = feature.get('survey');
    if (survey) {
      if (survey > surveyDateValues.lastMonthStart) {
        style = NodeStyle.smallSurveyLastMonth;
      } else if (survey > surveyDateValues.lastHalfYearStart) {
        style = NodeStyle.smallSurveyLastHalfYearStart;
      } else if (survey > surveyDateValues.lastYearStart) {
        style = NodeStyle.smallSurveyLastYearStart;
      } else if (survey > surveyDateValues.lastTwoYearsStart) {
        style = NodeStyle.smallSurveyLastTwoYearsStart;
      } else {
        style = NodeStyle.smallSurveyOlder;
      }
    }
    return style;
  }
}
