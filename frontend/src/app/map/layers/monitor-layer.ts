import { effect } from '@angular/core';
import { Signal } from '@angular/core';
import { MonitorLayerStyle } from '@app/map/style/monitor-layer-style';
import { ZoomLevel } from '@app/ol/domain/zoom-level';
import { MonitorMapState } from '@app/state/monitor/monitor-map-state';
import { MVT } from 'ol/format';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import { StyleFunction } from 'ol/style/Style';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class MonitorLayer {
  private monitorMapState: MonitorMapState; // local copy for performance reasons

  constructor(stateSignal: Signal<MonitorMapState>) {
    effect(() => {
      this.monitorMapState = stateSignal();
    });
  }

  build(): MapLayer {
    const source = new VectorTile({
      tileSize: 256,
      minZoom: ZoomLevel.newMinZoom,
      maxZoom: ZoomLevel.newMaxZoom,
      format: new MVT(),
      url: `/tiles/monitor/{z}/{x}/{y}.mvt`,
    });

    const layer = new VectorTileLayer({
      zIndex: Layers.zIndexMonitorLayer,
      source,
      renderBuffer: 40,
      declutter: false,
      className: 'poi',
      renderMode: 'vector',
    });

    layer.setStyle(this.styleFunction());

    return {
      layerType: 'monitor',
      minZoom: 2,
      maxZoom: 20,
      layer,
    };
  }

  private styleFunction(): StyleFunction {
    return (feature, resolution) => {
      return MonitorLayerStyle.style(this.monitorMapState, feature);
    };
  }
}
