import { ExploreStyleConstants } from '@app/map/style/explore-style-constants';
import { Marker } from '@app/ol/domain/marker';
import { OlUtil } from '@app/ol/ol-util';
import { FeatureLike } from 'ol/Feature';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import { MapStyleOptions } from '@app/state/map-style-options';

export class RouteDetailStyle {
  private static defaultRouteStyle = new Style({
    zIndex: ExploreStyleConstants.zIndexNodeRoute,
    stroke: new Stroke({
      color: '#00ff00',
      width: 3,
    }),
  });

  private static nodeMarker = Marker.createStyle('red');

  static style(styleOptions: MapStyleOptions, feature: FeatureLike): Style | Array<Style> {
    const featureLayer = OlUtil.featureLayer(feature);
    if (featureLayer === 'node') {
      return this.nodeStyle(styleOptions, feature);
    } else if (featureLayer === 'route' || featureLayer === 'node-route') {
      return this.routeStyle(styleOptions, feature);
    }
    return undefined;
  }

  private static nodeStyle(
    styleOptions: MapStyleOptions,
    feature: FeatureLike
  ): Style | Array<Style> {
    const nodeId = feature.get('id');
    if (styleOptions.focusElements.nodeIds.includes(nodeId)) {
      return this.nodeMarker;
    }
    return undefined;
  }

  private static routeStyle(
    styleOptions: MapStyleOptions,
    feature: FeatureLike
  ): Style | Array<Style> {
    const routeId = feature.get('routeId');
    if (styleOptions.focusElements.routeIds.includes(routeId)) {
      return this.defaultRouteStyle;
    }
    return undefined;
  }
}
