import { ExploreStyleConstants } from '@app/mapold/style/explore-style-constants';
import { Color } from 'ol/color';
import { FeatureLike } from 'ol/Feature';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import { MainMapStyleParameters } from './main-map-style-parameters';
import { RouteStyle } from './route-style';
import { StyleColor } from './style-color';

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
    const proposed = feature.get('state') === 'proposed';
    return this.routeStyleBuilder.style(color, resolution, proposed);
  }

  private initRouteSelectedStyle(): Style {
    return new Style({
      zIndex: ExploreStyleConstants.zIndexNodeRoute,
      stroke: new Stroke({
        color: StyleColor.selected,
        width: 14,
      }),
    });
  }

  private routeColor(parameters: MainMapStyleParameters, feature: FeatureLike): Color {
    let color = StyleColor.gray;
    if (parameters.mapMode === 'surface') {
      color = StyleColor.routeColorSurface(feature);
    } else if (parameters.mapMode === 'survey') {
      color = StyleColor.routeColorSurvey(parameters, feature);
    } else if (parameters.mapMode === 'analysis') {
      color = StyleColor.routeColorAnalysis(feature);
    }
    return color;
  }
}
