import { MonitorMapState } from '@app/state/monitor/monitor-map-state';
import { FeatureLike } from 'ol/Feature';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';

export class MonitorLayerStyle {
  private static readonly matchStyle = new Style({
    zIndex: 2,
    stroke: new Stroke({
      color: '#00aa00',
      width: 3,
    }),
  });
  private static readonly deviationStyle = new Style({
    zIndex: 2,
    stroke: new Stroke({
      color: '#ff0000',
      width: 3,
    }),
  });

  private static readonly referenceStyle = new Style({
    zIndex: 1,
    stroke: new Stroke({
      color: '#000000',
      width: 3,
    }),
  });

  static style(monitorMapState: MonitorMapState, feature: FeatureLike): Style | Array<Style> {
    const layer: string = feature.get('layer');
    const route: string = feature.get('route');
    const relationId: string = feature.get('relationId');

    if (
      monitorMapState.routeIds.includes(route) &&
      monitorMapState.relationIds.includes(+relationId)
    ) {
      if (layer == 'match') {
        if (monitorMapState.matchEnabled) {
          return this.matchStyle;
        }
      } else if (layer == 'actual') {
        if (monitorMapState.referenceEnabled) {
          return this.referenceStyle;
        }
      } else if (layer == 'deviation') {
        if (monitorMapState.deviationEnabled) {
          return this.deviationStyle;
        }
      }
    }
    return undefined;
  }
}
