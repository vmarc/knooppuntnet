import { effect } from '@angular/core';
import { Signal } from '@angular/core';
import { RouteType } from '@api/common/route-type';
import { MonitorRouteStyle } from '@app/map/style/monitor-route-style';
import { ZoomLevel } from '@app/ol/domain/zoom-level';
import { MonitorMapState } from '@app/state/monitor/monitor-map-state';
import { FeatureLike } from 'ol/Feature';
import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import { StyleFunction } from 'ol/style/Style';
import { MapStyleOptions } from '@app/state/map-style-options';
import { ExploreStyle } from '../style/explore-style';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class RouteLayer {
  // local copies for performance reasons
  private styleOptions: MapStyleOptions;
  private monitorMapState: MonitorMapState;

  constructor(
    styleOptionsSignal: Signal<MapStyleOptions>,
    monitorMapStateSignal: Signal<MonitorMapState>
  ) {
    effect(() => {
      this.styleOptions = styleOptionsSignal();
      this.monitorMapState = monitorMapStateSignal();
    });
  }

  build(routeType: RouteType): MapLayer {
    const source = new VectorTile({
      tileSize: 256,
      minZoom: ZoomLevel.newMinZoom,
      maxZoom: ZoomLevel.newMaxZoom,
      format: new MVT(),
      url: `/tiles/${routeType}/{z}/{x}/{y}.mvt`,
    });
    const layer = new VectorTileLayer({
      zIndex: Layers.zIndexRouteLayer,
      source: source,
      renderMode: 'vector',
      style: this.styleFunction(),
    });
    return {
      layerType: 'route',
      routeType: routeType,
      minZoom: 2,
      maxZoom: 20,
      layer: layer,
    };
  }

  private styleFunction(): StyleFunction {
    return (feature: FeatureLike) => {
      if (this.styleOptions.mode === 'monitor') {
        return MonitorRouteStyle.style(this.monitorMapState, feature);
      }
      return ExploreStyle.style(this.styleOptions, feature);
    };
  }
}
