import { MonitorMapState } from '@app/state/monitor/monitor-map-state';
import { FeatureLike } from 'ol/Feature';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';

export class MonitorLayerStyle {
  private static readonly referenceStyle = this.lineStyle('#0000ff');
  private static readonly matchStyle = this.lineStyle('#00aa00');
  private static readonly deviationStyle = this.lineStyle('#ff0000');

  static style(monitorMapState: MonitorMapState, feature: FeatureLike): Style | Array<Style> {
    const layer = feature.get('layer');
    const route = feature.get('route');

    if (monitorMapState.routeIds.includes(route)) {
      if (layer === 'match') {
        if (monitorMapState.matchEnabled) {
          return this.matchStyle;
        }
      } else if (layer === 'reference') {
        if (monitorMapState.referenceEnabled) {
          return this.referenceStyle;
        }
      } else if (layer === 'deviation') {
        if (monitorMapState.deviationEnabled) {
          return this.deviationStyle;
        }
      }
    }
    return undefined;
  }

  private static lineStyle(color: string): Style {
    return new Style({
      zIndex: 2,
      stroke: new Stroke({
        color: color,
        width: 3,
      }),
    });
  }
}
