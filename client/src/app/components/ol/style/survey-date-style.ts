import { Color } from 'ol/color';
import { FeatureLike } from 'ol/Feature';
import { Style } from 'ol/style';
import { MapService } from '../services/map.service';
import { darkRed } from './main-style-colors';
import { veryDarkGreen } from './main-style-colors';
import { darkGreen } from './main-style-colors';
import { green } from './main-style-colors';
import { lightGreen } from './main-style-colors';
import { gray } from './main-style-colors';
import { NodeStyle } from './node-style';

export class SurveyDateStyle {
  constructor(private mapService: MapService) {}

  surveyColor(feature: FeatureLike): Color {
    let color = gray; // survey date unknown
    const survey = feature.get('survey');
    if (survey) {
      if (survey > this.mapService.surveyDateInfo().lastMonthStart) {
        color = lightGreen;
      } else if (survey > this.mapService.surveyDateInfo().lastHalfYearStart) {
        color = green;
      } else if (survey > this.mapService.surveyDateInfo().lastYearStart) {
        color = darkGreen;
      } else if (survey > this.mapService.surveyDateInfo().lastTwoYearsStart) {
        color = veryDarkGreen;
      } else {
        color = darkRed;
      }
    }
    return color;
  }

  smallNodeStyle(feature: FeatureLike): Style {
    let style = NodeStyle.smallGray; // survey date unknown
    const survey = feature.get('survey');
    if (survey) {
      if (survey > this.mapService.surveyDateInfo().lastMonthStart) {
        style = NodeStyle.smallLightGreen;
      } else if (survey > this.mapService.surveyDateInfo().lastHalfYearStart) {
        style = NodeStyle.smallGreen;
      } else if (survey > this.mapService.surveyDateInfo().lastYearStart) {
        style = NodeStyle.smallDarkGreen;
      } else if (survey > this.mapService.surveyDateInfo().lastTwoYearsStart) {
        style = NodeStyle.smallVeryDarkGreen;
      } else {
        style = NodeStyle.smallDarkRed;
      }
    }
    return style;
  }
}
