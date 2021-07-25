import { FeatureLike } from 'ol/Feature';
import Circle from 'ol/style/Circle';
import Fill from 'ol/style/Fill';
import Style from 'ol/style/Style';
import { MapMode } from '../services/map-mode';
import { MapService } from '../services/map.service';
import { yellow } from './main-style-colors';
import { NodeStyle } from './node-style';
import { nameStyle } from './node-style-builder';
import { SurveyDateStyle } from './survey-date-style';

export class MainMapNodeStyle {
  private readonly largeMinZoomLevel = 13;
  private readonly smallNodeSelectedStyle = this.nodeSelectedStyle(8);
  private readonly largeNodeSelectedStyle = this.nodeSelectedStyle(20);
  private readonly surveyDateStyle: SurveyDateStyle;
  private readonly nameStyle = nameStyle();

  constructor(private mapService: MapService) {
    this.surveyDateStyle = new SurveyDateStyle(mapService);
  }

  nodeStyle(zoom: number, feature: FeatureLike): Array<Style> {
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

    const large = zoom >= this.largeMinZoomLevel;
    const styles = [];
    const selectedStyle = this.determineNodeSelectedStyle(featureId, large);
    if (selectedStyle) {
      styles.push(selectedStyle);
    }
    const style = this.determineNodeMainStyle(feature, large, title);
    styles.push(style);

    if (large && subTitle) {
      this.nameStyle.getText().setText(subTitle);
      styles.push(this.nameStyle);
    }

    return styles;
  }

  private determineNodeSelectedStyle(featureId: string, large: boolean): Style {
    let style = null;
    if (
      this.mapService.selectedNodeId &&
      featureId &&
      featureId === this.mapService.selectedNodeId
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
    feature: FeatureLike,
    large: boolean,
    title: string
  ): Style {
    let style: Style;
    if (large && '*' !== title) {
      style = this.determineLargeNodeStyle(feature, title);
    } else {
      style = this.determineSmallNodeStyle(feature);
    }
    return style;
  }

  private determineLargeNodeStyle(feature: FeatureLike, title: string): Style {
    const proposed = feature.get('proposed') === 'true';

    let style = NodeStyle.largeGray;
    if (proposed) {
      style = NodeStyle.proposedLargeGray;
    }

    if (this.mapService.mapMode === MapMode.surface) {
      if (proposed) {
        style = NodeStyle.proposedLargeGreen;
      } else {
        style = NodeStyle.largeGreen;
      }
    } else if (this.mapService.mapMode === MapMode.survey) {
      const survey = feature.get('survey');
      if (survey) {
        if (survey > this.mapService.surveyDateInfo().lastMonthStart) {
          if (proposed) {
            style = NodeStyle.proposedLargeLightGreen;
          } else {
            style = NodeStyle.largeLightGreen;
          }
        } else if (
          survey > this.mapService.surveyDateInfo().lastHalfYearStart
        ) {
          if (proposed) {
            style = NodeStyle.proposedLargeGreen;
          } else {
            style = NodeStyle.largeGreen;
          }
        } else if (survey > this.mapService.surveyDateInfo().lastYearStart) {
          if (proposed) {
            style = NodeStyle.proposedLargeDarkGreen;
          } else {
            style = NodeStyle.largeDarkGreen;
          }
        } else if (
          survey > this.mapService.surveyDateInfo().lastTwoYearsStart
        ) {
          if (proposed) {
            style = NodeStyle.proposedLargeVeryDarkGreen;
          } else {
            style = NodeStyle.largeVeryDarkGreen;
          }
        } else {
          if (proposed) {
            style = NodeStyle.proposedLargeDarkRed;
          } else {
            style = NodeStyle.largeDarkRed;
          }
        }
      }
    } else if (this.mapService.mapMode === MapMode.analysis) {
      const layer = feature.get('layer');
      if ('error-node' === layer) {
        if (proposed) {
          style = NodeStyle.proposedLargeBlue;
        } else {
          style = NodeStyle.largeBlue;
        }
      } else if ('orphan-node' === layer) {
        if (proposed) {
          style = NodeStyle.proposedLargeDarkGreen;
        } else {
          style = NodeStyle.largeDarkGreen;
        }
      } else if ('error-orphan-node' === layer) {
        if (proposed) {
          style = NodeStyle.proposedLargeDarkBlue;
        } else {
          style = NodeStyle.largeDarkBlue;
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

  private determineSmallNodeStyle(feature: FeatureLike): Style {
    let style = NodeStyle.smallGray;
    if (this.mapService.mapMode === MapMode.surface) {
      style = NodeStyle.smallGreen;
    } else if (this.mapService.mapMode === MapMode.survey) {
      style = this.surveyDateStyle.smallNodeStyle(feature);
    } else if (this.mapService.mapMode === MapMode.analysis) {
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
    } else if ('orphan-node' === layer) {
      style = NodeStyle.smallDarkGreen;
    } else if ('error-orphan-node' === layer) {
      style = NodeStyle.smallDarkRed;
    } else {
      style = NodeStyle.smallGreen;
    }
    return style;
  }
}
