import { SurveyDateValues } from '@app/shared/core/shared/survey-date-values';
import { Color } from 'ol/color';
import { FeatureLike } from 'ol/Feature';
import { Style } from 'ol/style';
import { MainMapStyleParameters } from './main-map-style-parameters';
import { NodeStyle } from './node-style';
import { StyleColor } from './style-color';

export class SurveyDateStyle {
  static surveyColor(surveyDateValues: SurveyDateValues, feature: FeatureLike): Color {
    let color = StyleColor.surveyUnknown;
    const survey = feature.get('survey');
    if (survey) {
      if (survey > surveyDateValues.lastMonthStart) {
        color = StyleColor.surveyLastMonth;
      } else if (survey > surveyDateValues.lastHalfYearStart) {
        color = StyleColor.surveyLastHalfYearStart;
      } else if (survey > surveyDateValues.lastYearStart) {
        color = StyleColor.surveyLastYearStart;
      } else if (survey > surveyDateValues.lastTwoYearsStart) {
        color = StyleColor.surveyLastTwoYearsStart;
      } else {
        color = StyleColor.surveyOlder;
      }
    }
    return color;
  }

  static largeNodeStyle(
    feature: FeatureLike,
    parameters: MainMapStyleParameters,
    proposed: boolean
  ): Style {
    let style = NodeStyle.surveyUnknownLarge;
    if (proposed) {
      style = NodeStyle.surveyUnknownProposedLarge;
    }
    const survey = feature.get('survey');
    if (survey) {
      if (survey > parameters.surveyDateValues.lastMonthStart) {
        if (proposed) {
          style = NodeStyle.surveyLastMonthProposedLarge;
        } else {
          style = NodeStyle.surveyLastMonthLarge;
        }
      } else if (survey > parameters.surveyDateValues.lastHalfYearStart) {
        if (proposed) {
          style = NodeStyle.surveyLastHalfYearStartProposedLarge;
        } else {
          style = NodeStyle.surveyLastHalfYearStartLarge;
        }
      } else if (survey > parameters.surveyDateValues.lastYearStart) {
        if (proposed) {
          style = NodeStyle.surveyLastYearStartProposedLarge;
        } else {
          style = NodeStyle.surveyLastYearStartLarge;
        }
      } else if (survey > parameters.surveyDateValues.lastTwoYearsStart) {
        if (proposed) {
          style = NodeStyle.surveyLastTwoYearsStartProposedLarge;
        } else {
          style = NodeStyle.surveyLastTwoYearsStartLarge;
        }
      } else {
        if (proposed) {
          style = NodeStyle.surveyOlderProposedLarge;
        } else {
          style = NodeStyle.surveyOlderLarge;
        }
      }
    }
    return style;
  }

  static smallNodeStyle(feature: FeatureLike, parameters: MainMapStyleParameters): Style {
    let style = NodeStyle.surveyUnknownSmall; // survey date unknown
    const survey = feature.get('survey');
    if (survey) {
      if (survey > parameters.surveyDateValues.lastMonthStart) {
        style = NodeStyle.surveyLastMonthSmall;
      } else if (survey > parameters.surveyDateValues.lastHalfYearStart) {
        style = NodeStyle.surveyLastHalfYearStartSmall;
      } else if (survey > parameters.surveyDateValues.lastYearStart) {
        style = NodeStyle.surveyLastYearStartSmall;
      } else if (survey > parameters.surveyDateValues.lastTwoYearsStart) {
        style = NodeStyle.surveyLastTwoYearsStartSmall;
      } else {
        style = NodeStyle.surveyOlderSmall;
      }
    }
    return style;
  }
}
