import { MainMapStyleOptions } from '@app/components/ol/style/main-map-style-options';
import { FeatureLike } from 'ol/Feature';
import Circle from 'ol/style/Circle';
import Fill from 'ol/style/Fill';
import Style from 'ol/style/Style';
import { yellow } from './main-style-colors';
import { NodeStyle } from './node-style';
import { nameStyle } from './node-style-builder';
import { SurveyDateStyle } from './survey-date-style';

export class MainMapNodeStyle {
  private readonly largeMaxResolution = 19.109; // zoomLevel 13
  private readonly smallNodeSelectedStyle = this.nodeSelectedStyle(8);
  private readonly largeNodeSelectedStyle = this.nodeSelectedStyle(20);
  private readonly nameStyle = nameStyle();

  constructor() {}

  nodeStyle(
    options: MainMapStyleOptions,
    resolution: number,
    feature: FeatureLike
  ): Array<Style> {
    const featureId = feature.get('id');
    const ref = feature.get('ref');
    const name = feature.get('name');

    let title: string;
    let subTitle: string;

    if (ref && ref !== 'o') {
      title = ref;
      subTitle = name;
    } else {
      title = name;
    }

    const large = resolution < this.largeMaxResolution;
    const styles = [];
    const selectedStyle = this.determineNodeSelectedStyle(
      options,
      featureId,
      large
    );
    if (selectedStyle) {
      styles.push(selectedStyle);
    }
    const style = this.determineNodeMainStyle(options, feature, large, title);
    styles.push(style);

    if (large && subTitle) {
      this.nameStyle.getText().setText(subTitle);
      styles.push(this.nameStyle);
    }

    return styles;
  }

  private determineNodeSelectedStyle(
    options: MainMapStyleOptions,
    featureId: string,
    large: boolean
  ): Style {
    let style = null;
    if (
      options.selectedNodeId &&
      featureId &&
      featureId === options.selectedNodeId
    ) {
      if (large) {
        style = this.largeNodeSelectedStyle;
      } else {
        style = this.smallNodeSelectedStyle;
      }
    }
    return style;
  }

  private determineNodeMainStyle(
    options: MainMapStyleOptions,
    feature: FeatureLike,
    large: boolean,
    title: string
  ): Style {
    let style: Style;
    if (large && '*' !== title) {
      style = this.determineLargeNodeStyle(options, feature, title);
    } else {
      style = this.determineSmallNodeStyle(options, feature);
    }
    return style;
  }

  private determineLargeNodeStyle(
    options: MainMapStyleOptions,
    feature: FeatureLike,
    title: string
  ): Style {
    const proposed = feature.get('proposed') === 'true';

    let style = NodeStyle.largeGray;
    if (proposed) {
      style = NodeStyle.proposedLargeGray;
    }

    if (options.mapMode === 'surface') {
      if (proposed) {
        style = NodeStyle.proposedLargeGreen;
      } else {
        style = NodeStyle.largeGreen;
      }
    } else if (options.mapMode === 'survey') {
      style = NodeStyle.largeSurveyUnknown;
      const survey = feature.get('survey');
      if (survey) {
        if (survey > options.surveyDateValues.lastMonthStart) {
          if (proposed) {
            style = NodeStyle.proposedLargeLightGreen;
          } else {
            style = NodeStyle.largeSurveyLastMonth;
          }
        } else if (survey > options.surveyDateValues.lastHalfYearStart) {
          if (proposed) {
            style = NodeStyle.proposedLargeGreen;
          } else {
            style = NodeStyle.largeSurveyLastHalfYearStart;
          }
        } else if (survey > options.surveyDateValues.lastYearStart) {
          if (proposed) {
            style = NodeStyle.proposedLargeDarkGreen;
          } else {
            style = NodeStyle.largeSurveyLastYearStart;
          }
        } else if (survey > options.surveyDateValues.lastTwoYearsStart) {
          if (proposed) {
            style = NodeStyle.proposedLargeVeryDarkGreen;
          } else {
            style = NodeStyle.largeSurveyLastTwoYearsStart;
          }
        } else {
          if (proposed) {
            style = NodeStyle.proposedLargeDarkRed;
          } else {
            style = NodeStyle.largeSurveyOlder;
          }
        }
      }
    } else if (options.mapMode === 'analysis') {
      const layer = feature.get('layer');
      if ('error-node' === layer) {
        if (proposed) {
          style = NodeStyle.proposedLargeBlue;
        } else {
          style = NodeStyle.largeBlue;
        }
      } else {
        if (proposed) {
          style = NodeStyle.proposedLargeGreen;
        } else {
          style = NodeStyle.largeGreen;
        }
      }
    }

    style.getText().setText(title);
    return style;
  }

  private determineSmallNodeStyle(
    options: MainMapStyleOptions,
    feature: FeatureLike
  ): Style {
    let style = NodeStyle.smallGray;
    if (options.mapMode === 'surface') {
      style = NodeStyle.smallGreen;
    } else if (options.mapMode === 'survey') {
      style = SurveyDateStyle.smallNodeStyle(options.surveyDateValues, feature);
    } else if (options.mapMode === 'analysis') {
      style = this.smallNodeStyleAnalysis(feature);
    }
    return style;
  }

  private nodeSelectedStyle(radius: number): Style {
    return new Style({
      image: new Circle({
        radius,
        fill: new Fill({
          color: yellow,
        }),
      }),
    });
  }

  private smallNodeStyleAnalysis(feature: FeatureLike): Style {
    const layer = feature.get('layer');
    let style: Style;
    if ('error-node' === layer) {
      style = NodeStyle.smallRed;
    } else {
      style = NodeStyle.smallGreen;
    }
    return style;
  }
}
