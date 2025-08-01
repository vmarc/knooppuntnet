import { RouteSegmentStyle } from '@app/map/style/route-segment-style';
import { MapStyleOptions } from '@app/state/map-style-options';
import { FeatureLike } from 'ol/Feature';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';

export class RouteSegmentsStyle {
  private static readonly segmentBackgroundStyle = new Style({
    zIndex: 1,
    stroke: new Stroke({
      color: '#fff',
      width: 8,
    }),
  });

  static style(styleOptions: MapStyleOptions, feature: FeatureLike): Style | Array<Style> {
    const routeId: string = feature.get('routeId');
    if (routeId && styleOptions.focusElements.routeIds.includes(routeId)) {
      const segmentId = feature.get('segmentId');
      const color = styleOptions.segmentMap.color(routeId, segmentId);
      if (color) {
        return [this.segmentBackgroundStyle, RouteSegmentStyle.routeStyle(color)];
      }
    }
    return undefined;
  }
}
