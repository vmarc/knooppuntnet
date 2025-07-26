import { MonitorMapState } from '@app/state/monitor/monitor-map-state';
import { FeatureLike } from 'ol/Feature';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';

export class MonitorRouteStyle {
  private static readonly routeStyle = new Style({
    zIndex: 1,
    stroke: new Stroke({
      color: '#fff',
      width: 16,
    }),
  });

  static style(monitorMapState: MonitorMapState, feature: FeatureLike): Style | Array<Style> {
    const routeId = +feature.get('routeId');
    if (monitorMapState.relationIds.includes(routeId)) {
      return this.routeStyle;
    }
    return undefined;
  }
}
