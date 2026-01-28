import { RouteSegmentStyle } from '@app/mapold/style/route-segment-style';
import { MapStyleOptions } from '@app/state/map-style-options';
import { MonitorMapState } from '@app/state/monitor/monitor-map-state';
import { FeatureLike } from 'ol/Feature';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';

export class MonitorLayerStyle {
  private static readonly matchStyle = new Style({
    zIndex: 3,
    stroke: new Stroke({
      color: '#00aa00',
      width: 3,
    }),
  });
  private static readonly deviationStyle = new Style({
    zIndex: 3,
    stroke: new Stroke({
      color: '#ff0000',
      width: 3,
    }),
  });

  private static readonly routeStyle = new Style({
    zIndex: 2,
    stroke: new Stroke({
      color: '#ff0',
      width: 8,
    }),
  });

  private static readonly segmentBackgroundStyle = new Style({
    zIndex: 1,
    stroke: new Stroke({
      color: '#fff',
      width: 8,
    }),
  });

  static style(
    styleOptions: MapStyleOptions,
    monitorMapState: MonitorMapState,
    feature: FeatureLike
  ): Style | Array<Style> {
    if (monitorMapState.mode === 'segments') {
      return this.segmentsModeStyle(styleOptions, monitorMapState, feature);
    }
    return this.routeModeStyle(monitorMapState, feature);
  }

  private static routeModeStyle(
    monitorMapState: MonitorMapState,
    feature: FeatureLike
  ): Style | Array<Style> {
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
      } else if (layer == 'route') {
        if (monitorMapState.routeEnabled) {
          return this.routeStyle;
        }
      } else if (layer == 'deviation') {
        if (monitorMapState.deviationEnabled) {
          return this.deviationStyle;
        }
      }
    }
    return undefined;
  }

  private static segmentsModeStyle(
    styleOptions: MapStyleOptions,
    monitorMapState: MonitorMapState,
    feature: FeatureLike
  ): Style | Array<Style> {
    if (!monitorMapState.monitorShowSegments) {
      return undefined;
    }

    const layer: string = feature.get('layer');
    const route: string = feature.get('route');
    const relationId: string = feature.get('relationId');
    const segmentId: string = feature.get('segmentId');

    if (
      monitorMapState.routeIds.includes(route) &&
      monitorMapState.relationIds.includes(+relationId)
    ) {
      if (layer == 'match') {
        if (monitorMapState.matchEnabled) {
          return this.segmentBackgroundStyle;
        }
      } else if (layer == 'route') {
        if (monitorMapState.routeEnabled) {
          const color = styleOptions.segmentMap.color(relationId, segmentId);
          if (color) {
            return [this.segmentBackgroundStyle, RouteSegmentStyle.routeStyle(color)];
          }
        }
      } else if (layer == 'deviation') {
        if (monitorMapState.deviationEnabled) {
          return this.segmentBackgroundStyle;
        }
      }
    }
    return undefined;
  }
}
