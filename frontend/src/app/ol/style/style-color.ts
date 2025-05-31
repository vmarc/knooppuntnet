import { OlUtil } from '@app/ol/ol-util';
import { Color } from 'ol/color';
import { FeatureLike } from 'ol/Feature';
import { MainMapStyleParameters } from './main-map-style-parameters';
import { SurveyDateStyle } from './survey-date-style';

export class StyleColor {
  static readonly surfacePaved: Color = [0, 96, 255]; // light blue
  static readonly surfaceUnpaved: Color = [0, 240, 0]; // green (not super bright)
  static readonly surfaceUnknown: Color = [255, 176, 0]; // light orange

  static readonly surveyUnknown: Color = [255, 255, 0]; // yellow
  static readonly surveyUnknownNode: Color = [225, 225, 0]; // yellow
  static readonly surveyLastMonth: Color = [0, 255, 0]; // light green
  static readonly surveyLastHalfYearStart: Color = [0, 200, 0]; // green
  static readonly surveyLastYearStart: Color = [0, 150, 0]; // = dark green
  static readonly surveyLastTwoYearsStart: Color = [0, 90, 0]; // very dark green
  static readonly surveyOlder: Color = [187, 0, 0]; // dark red

  static readonly analysisOk: Color = [0, 200, 0]; // green
  static readonly analysisError: Color = [255, 0, 0]; // red

  static readonly networkIn: Color = [0, 200, 0]; // green
  static readonly networkOut: Color = [200, 200, 200]; // gray
  static readonly networkConnection: Color = [255, 150, 0]; // orange/brown

  static readonly gray: Color = [200, 200, 200]; // gray
  static readonly selected: Color = [255, 255, 0]; // yellow

  static readonly defaultColor: Color = [0, 200, 0]; // green

  static routeColorAnalysis(feature: FeatureLike): Color {
    const featureLayer = OlUtil.featureLayer(feature);
    let color = StyleColor.gray;
    if ('route' === featureLayer) {
      color = StyleColor.analysisOk;
    } else if ('incomplete-route' === featureLayer) {
      color = StyleColor.analysisError;
    } else if ('error-route' === featureLayer) {
      color = StyleColor.analysisError;
    }
    return color;
  }

  static routeColorSurface(feature: FeatureLike): Color {
    const surface = feature.get('surface');
    let color = StyleColor.surfacePaved;
    if ('unpaved' === surface) {
      color = StyleColor.surfaceUnpaved;
    } else if ('unknown' === surface) {
      color = StyleColor.surfaceUnknown;
    }
    return color;
  }

  static routeColorSurvey(parameters: MainMapStyleParameters, feature: FeatureLike): Color {
    return SurveyDateStyle.surveyColor(parameters.surveyDateValues, feature);
  }
}

export const green: Color = [0, 255, 0]; //regular nodes and routes

export const red: Color = [255, 0, 0]; // orphan
export const blue: Color = [0, 0, 255]; // orphan error

export const white: Color = [255, 255, 255]; // node inner color
export const orange: Color = [255, 165, 0];

export const proposedWhite: Color = [240, 240, 240];
