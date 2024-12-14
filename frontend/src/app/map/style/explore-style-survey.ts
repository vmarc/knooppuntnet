import { SurveyDateValues } from '@app/core';
import { proposedLarge } from '@app/ol/style';
import { large } from '@app/ol/style';
import { StyleColor } from '@app/ol/style';
import { small } from '@app/ol/style';
import { Color } from 'ol/color';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';

export class ExploreStyleSurvey {
  private static readonly surveyUnknown: Color = [255, 255, 0]; // yellow
  private static readonly surveyUnknownNode: Color = [225, 225, 0]; // yellow
  private static readonly surveyLastMonth: Color = [0, 255, 0]; // light green
  private static readonly surveyLastHalfYearStart: Color = [0, 200, 0]; // green
  private static readonly surveyLastYearStart: Color = [0, 150, 0]; // = dark green
  private static readonly surveyLastTwoYearsStart: Color = [0, 90, 0]; // very dark green
  private static readonly surveyOlder: Color = [187, 0, 0]; // dark red

  static readonly surveyUnknownSmall = small(StyleColor.surveyUnknownNode);
  static readonly surveyLastMonthSmall = small(StyleColor.surveyLastMonth);
  static readonly surveyLastHalfYearStartSmall = small(StyleColor.surveyLastHalfYearStart);
  static readonly surveyLastYearStartSmall = small(StyleColor.surveyLastYearStart);
  static readonly surveyLastTwoYearsStartSmall = small(StyleColor.surveyLastTwoYearsStart);
  static readonly surveyOlderSmall = small(StyleColor.surveyOlder);

  static readonly surveyUnknownLarge = large(StyleColor.surveyUnknownNode);
  static readonly surveyLastMonthLarge = large(StyleColor.surveyLastMonth);
  static readonly surveyLastHalfYearStartLarge = large(StyleColor.surveyLastHalfYearStart);
  static readonly surveyLastYearStartLarge = large(StyleColor.surveyLastYearStart);
  static readonly surveyLastTwoYearsStartLarge = large(StyleColor.surveyLastTwoYearsStart);
  static readonly surveyOlderLarge = large(StyleColor.surveyOlder);

  static readonly surveyUnknownProposedLarge = proposedLarge(StyleColor.surveyUnknownNode);
  static readonly surveyLastMonthProposedLarge = proposedLarge(StyleColor.surveyLastMonth);
  static readonly surveyLastHalfYearStartProposedLarge = proposedLarge(
    StyleColor.surveyLastHalfYearStart
  );
  static readonly surveyLastYearStartProposedLarge = proposedLarge(StyleColor.surveyLastYearStart);
  static readonly surveyLastTwoYearsStartProposedLarge = proposedLarge(
    StyleColor.surveyLastTwoYearsStart
  );
  static readonly surveyOlderProposedLarge = proposedLarge(StyleColor.surveyOlder);

  private static readonly surveyStyle = new Style({
    zIndex: 11,
    stroke: new Stroke({
      color: '#ffff00',
      width: 4,
    }),
  });

  static nodeStyle(
    zoom: number,
    surveyDateValues: SurveyDateValues,
    survey: string,
    proposed: boolean,
    title: string
  ): Style {
    let style: Style | undefined = undefined;
    if (zoom >= 13) {
      style = this.largeNodeStyle(surveyDateValues, survey, proposed);
      style.getText().setText(title);
    } else if (zoom >= 10) {
      style = this.smallNodeStyle(surveyDateValues, survey);
    }
    console.log('nodeStyle', style);

    return style;
  }

  static routeStyle(zoom: number, surveyDateValues: SurveyDateValues, survey: string): Style {
    const color = this.surveyColor(surveyDateValues, survey);
    const style = this.surveyStyle;
    style.getStroke().setColor(color);
    return style;
  }

  private static surveyColor(surveyDateValues: SurveyDateValues, survey: string): Color {
    let color = this.surveyUnknown;
    if (survey) {
      if (survey > surveyDateValues.lastMonthStart) {
        color = this.surveyLastMonth;
      } else if (survey > surveyDateValues.lastHalfYearStart) {
        color = this.surveyLastHalfYearStart;
      } else if (survey > surveyDateValues.lastYearStart) {
        color = this.surveyLastYearStart;
      } else if (survey > surveyDateValues.lastTwoYearsStart) {
        color = this.surveyLastTwoYearsStart;
      } else {
        color = this.surveyOlder;
      }
    }
    return color;
  }

  private static largeNodeStyle(
    surveyDateValues: SurveyDateValues,
    survey: string,
    proposed: boolean
  ): Style {
    let style = this.surveyUnknownLarge;
    if (proposed) {
      style = this.surveyUnknownProposedLarge;
    }
    if (survey) {
      if (survey > surveyDateValues.lastMonthStart) {
        if (proposed) {
          style = this.surveyLastMonthProposedLarge;
        } else {
          style = this.surveyLastMonthLarge;
        }
      } else if (survey > surveyDateValues.lastHalfYearStart) {
        if (proposed) {
          style = this.surveyLastHalfYearStartProposedLarge;
        } else {
          style = this.surveyLastHalfYearStartLarge;
        }
      } else if (survey > surveyDateValues.lastYearStart) {
        if (proposed) {
          style = this.surveyLastYearStartProposedLarge;
        } else {
          style = this.surveyLastYearStartLarge;
        }
      } else if (survey > surveyDateValues.lastTwoYearsStart) {
        if (proposed) {
          style = this.surveyLastTwoYearsStartProposedLarge;
        } else {
          style = this.surveyLastTwoYearsStartLarge;
        }
      } else {
        if (proposed) {
          style = this.surveyOlderProposedLarge;
        } else {
          style = this.surveyOlderLarge;
        }
      }
    }
    return style;
  }

  private static smallNodeStyle(surveyDateValues: SurveyDateValues, survey: string): Style {
    let style = this.surveyUnknownSmall; // survey date unknown
    if (survey) {
      if (survey > surveyDateValues.lastMonthStart) {
        style = this.surveyLastMonthSmall;
      } else if (survey > surveyDateValues.lastHalfYearStart) {
        style = this.surveyLastHalfYearStartSmall;
      } else if (survey > surveyDateValues.lastYearStart) {
        style = this.surveyLastYearStartSmall;
      } else if (survey > surveyDateValues.lastTwoYearsStart) {
        style = this.surveyLastTwoYearsStartSmall;
      } else {
        style = this.surveyOlderSmall;
      }
    }
    return style;
  }
}
