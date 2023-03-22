import { MainMapStyleParameters } from '@app/components/ol/style/main-map-style-parameters';
import { Color } from 'ol/color';
import { FeatureLike } from 'ol/Feature';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import { surfaceUnknownColor } from './main-style-colors';
import { proposedSurfaceUnknownColor } from './main-style-colors';
import { proposedUnpavedColor } from './main-style-colors';
import { proposedColor } from './main-style-colors';
import { red } from './main-style-colors';
import { orange } from './main-style-colors';
import { gray } from './main-style-colors';
import { yellow } from './main-style-colors';
import { green } from './main-style-colors';
import { RouteStyle } from './route-style';
import { SurveyDateStyle } from './survey-date-style';

export class MainMapRouteStyle {
  private readonly routeStyleBuilder = new RouteStyle();
  private readonly defaultRouteSelectedStyle = this.initRouteSelectedStyle();

  public routeStyle(
    parameters: MainMapStyleParameters,
    resolution: number,
    feature: FeatureLike
  ): Array<Style> {
    const selectedStyle = this.determineRouteSelectedStyle(parameters, feature);
    const style = this.determineRouteStyle(parameters, feature, resolution);
    return selectedStyle ? [selectedStyle, style] : [style];
  }

  private determineRouteSelectedStyle(
    parameters: MainMapStyleParameters,
    feature: FeatureLike
  ): Style {
    const featureId = feature.get('id');
    let style = null;
    if (
      parameters.selectedRouteId &&
      featureId &&
      featureId.startsWith(parameters.selectedRouteId)
    ) {
      style = this.defaultRouteSelectedStyle;
    }
    return style;
  }

  private determineRouteStyle(
    parameters: MainMapStyleParameters,
    feature: FeatureLike,
    resolution: number
  ): Style {
    const color = this.routeColor(parameters, feature);
    const highligthed =
      parameters.highlightedRouteId &&
      feature.get('id').startsWith(parameters.highlightedRouteId);
    const proposed = feature.get('state') === 'proposed';
    return this.routeStyleBuilder.style(
      color,
      resolution,
      highligthed,
      proposed
    );
  }

  private initRouteSelectedStyle(): Style {
    return new Style({
      stroke: new Stroke({
        color: yellow,
        width: 14,
      }),
    });
  }

  private routeColor(
    parameters: MainMapStyleParameters,
    feature: FeatureLike
  ): Color {
    let color = gray;
    if (parameters.mapMode === 'surface') {
      color = this.routeColorSurface(feature);
    } else if (parameters.mapMode === 'survey') {
      color = this.routeColorSurvey(parameters, feature);
    } else if (parameters.mapMode === 'analysis') {
      color = this.routeColorAnalysis(feature);
    }
    return color;
  }

  private routeColorSurface(feature: FeatureLike): Color {
    const proposed = feature.get('state') === 'proposed';
    const surface = feature.get('surface');
    let color = green;
    if ('unpaved' === surface) {
      if (proposed) {
        color = proposedUnpavedColor;
      } else {
        color = orange;
      }
    } else if ('unknown' === surface) {
      if (proposed) {
        color = proposedSurfaceUnknownColor;
      } else {
        color = surfaceUnknownColor;
      }
    } else {
      if (proposed) {
        color = proposedColor;
      }
    }
    return color;
  }

  private routeColorSurvey(
    parameters: MainMapStyleParameters,
    feature: FeatureLike
  ): Color {
    return SurveyDateStyle.surveyColor(parameters.surveyDateValues, feature);
  }

  private routeColorAnalysis(feature: FeatureLike): Color {
    const layer = feature.get('layer');
    let color = gray;
    if ('route' === layer) {
      color = green;
    } else if ('incomplete-route' === layer) {
      color = red;
    } else if ('error-route' === layer) {
      color = red;
    }
    return color;
  }
}
