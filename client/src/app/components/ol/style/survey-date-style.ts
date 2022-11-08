import { Color } from 'ol/color';
import { FeatureLike } from 'ol/Feature';
import { Style } from 'ol/style';
import { MapService } from '../services/map.service';
import { surveyOlder } from './main-style-colors';
import { surveyLastTwoYearsStart } from './main-style-colors';
import { surveyLastYearStart } from './main-style-colors';
import { surveyLastMonth } from './main-style-colors';
import { surveyLastHalfYearStart } from './main-style-colors';
import { surveyUnknown } from './main-style-colors';
import { NodeStyle } from './node-style';

export class SurveyDateStyle {
  constructor(private mapService: MapService) {}

  surveyColor(feature: FeatureLike): Color {
    let color = surveyUnknown; // survey date unknown
    const survey = feature.get('survey');
    if (survey) {
      if (survey > this.mapService.surveyDateInfo().lastMonthStart) {
        color = surveyLastMonth;
      } else if (survey > this.mapService.surveyDateInfo().lastHalfYearStart) {
        color = surveyLastHalfYearStart;
      } else if (survey > this.mapService.surveyDateInfo().lastYearStart) {
        color = surveyLastYearStart;
      } else if (survey > this.mapService.surveyDateInfo().lastTwoYearsStart) {
        color = surveyLastTwoYearsStart;
      } else {
        color = surveyOlder;
      }
    }
    return color;
  }

  smallNodeStyle(feature: FeatureLike): Style {
    let style = NodeStyle.smallSurveyUnknown; // survey date unknown
    const survey = feature.get('survey');
    if (survey) {
      if (survey > this.mapService.surveyDateInfo().lastMonthStart) {
        style = NodeStyle.smallSurveyLastMonth;
      } else if (survey > this.mapService.surveyDateInfo().lastHalfYearStart) {
        style = NodeStyle.smallSurveyLastHalfYearStart;
      } else if (survey > this.mapService.surveyDateInfo().lastYearStart) {
        style = NodeStyle.smallSurveyLastYearStart;
      } else if (survey > this.mapService.surveyDateInfo().lastTwoYearsStart) {
        style = NodeStyle.smallSurveyLastTwoYearsStart;
      } else {
        style = NodeStyle.smallSurveyOlder;
      }
    }
    return style;
  }
}
