import { MainMapStyleParameters } from '@app/components/ol/style/main-map-style-parameters';
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
    parameters: MainMapStyleParameters,
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
      parameters,
      featureId,
      large
    );
    if (selectedStyle) {
      styles.push(selectedStyle);
    }
    const style = this.determineNodeMainStyle(
      parameters,
      feature,
      large,
      title
    );
    styles.push(style);

    if (large && subTitle) {
      this.nameStyle.getText().setText(subTitle);
      styles.push(this.nameStyle);
    }

    return styles;
  }

  private determineNodeSelectedStyle(
    parameters: MainMapStyleParameters,
    featureId: string,
    large: boolean
  ): Style {
    let style = null;
    if (
      parameters.selectedNodeId &&
      featureId &&
      featureId === parameters.selectedNodeId
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
    parameters: MainMapStyleParameters,
    feature: FeatureLike,
    large: boolean,
    title: string
  ): Style {
    let style: Style;
    if (large && '*' !== title) {
      style = this.determineLargeNodeStyle(parameters, feature, title);
    } else {
      style = this.determineSmallNodeStyle(parameters, feature);
    }
    return style;
  }

  private determineLargeNodeStyle(
    parameters: MainMapStyleParameters,
    feature: FeatureLike,
    title: string
  ): Style {
    const proposed = feature.get('proposed') === 'true';

    let style = NodeStyle.largeGray;
    if (proposed) {
      style = NodeStyle.proposedLargeGray;
    }

    if (parameters.mapMode === 'surface') {
      if (proposed) {
        style = NodeStyle.proposedLargeGreen;
      } else {
        style = NodeStyle.largeGreen;
      }
    } else if (parameters.mapMode === 'survey') {
      style = NodeStyle.largeSurveyUnknown;
      const survey = feature.get('survey');
      if (survey) {
        if (survey > parameters.surveyDateValues.lastMonthStart) {
          if (proposed) {
            style = NodeStyle.proposedLargeLightGreen;
          } else {
            style = NodeStyle.largeSurveyLastMonth;
          }
        } else if (survey > parameters.surveyDateValues.lastHalfYearStart) {
          if (proposed) {
            style = NodeStyle.proposedLargeGreen;
          } else {
            style = NodeStyle.largeSurveyLastHalfYearStart;
          }
        } else if (survey > parameters.surveyDateValues.lastYearStart) {
          if (proposed) {
            style = NodeStyle.proposedLargeDarkGreen;
          } else {
            style = NodeStyle.largeSurveyLastYearStart;
          }
        } else if (survey > parameters.surveyDateValues.lastTwoYearsStart) {
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
    } else if (parameters.mapMode === 'analysis') {
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
    parameters: MainMapStyleParameters,
    feature: FeatureLike
  ): Style {
    let style = NodeStyle.smallGray;
    if (parameters.mapMode === 'surface') {
      style = NodeStyle.smallGreen;
    } else if (parameters.mapMode === 'survey') {
      style = SurveyDateStyle.smallNodeStyle(
        parameters.surveyDateValues,
        feature
      );
    } else if (parameters.mapMode === 'analysis') {
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
