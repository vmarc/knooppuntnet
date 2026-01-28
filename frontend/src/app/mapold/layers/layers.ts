import { computed } from '@angular/core';
import { effect } from '@angular/core';
import { Signal } from '@angular/core';
import { MonitorLayer } from '@app/mapold/layers/monitor-layer';
import { MonitorMapState } from '@app/state/monitor/monitor-map-state';
import { State } from '@app/state/state';
import { LayersState } from '@app/state/layers-state';
import { MapStyleOptions } from '@app/state/map-style-options';
import { PoiStyleMap } from '@app/state/poi/poi-style-map';
import { OpendataTileLayer } from './opendata-tile-layer';
import { StandardBackground } from './standard-background';
import { GridLayer } from './grid-layer';
import { MapLayer } from './map-layer';
import { OsmBackgroundLayer } from './osm-background-layer';
import { PoiLayer } from './poi-layer';
import { RouteLayer } from './route-layer';

export class Layers {
  static readonly zIndexRouteLayer = 60;
  static readonly zIndexPoiLayer = 40;
  static readonly zIndexMonitorLayer = 80;

  readonly poiLayer: MapLayer;
  readonly all: ReadonlyArray<MapLayer>;

  constructor(
    state: State,
    styleOptions: Signal<MapStyleOptions>,
    monitorMapState: Signal<MonitorMapState>,
    poiStyleMap: Signal<PoiStyleMap>,
    poiActive: Signal<ReadonlyMap<string, boolean>>
  ) {
    this.poiLayer = PoiLayer.build(poiStyleMap, poiActive);
    this.all = [
      StandardBackground.build(),
      OsmBackgroundLayer.build(),
      GridLayer.build(),
      new RouteLayer(styleOptions, monitorMapState).build('cycling'),
      new RouteLayer(styleOptions, monitorMapState).build('hiking'),
      new RouteLayer(styleOptions, monitorMapState).build('horse-riding'),
      new RouteLayer(styleOptions, monitorMapState).build('motorboat'),
      new RouteLayer(styleOptions, monitorMapState).build('canoe'),
      new RouteLayer(styleOptions, monitorMapState).build('inline-skating'),
      this.poiLayer,
      OpendataTileLayer.build('flanders-open-data', 'hiking', 'flanders/hiking'),
      OpendataTileLayer.build('flanders-open-data', 'cycling', 'flanders/cycling'),
      OpendataTileLayer.build('netherlands-open-data', 'hiking', 'netherlands/hiking'),
      OpendataTileLayer.build('netherlands-open-data', 'cycling', 'netherlands/cycling'),
      OpendataTileLayer.build('france-open-data', 'hiking', 'france/hiking'),
      new MonitorLayer(styleOptions, monitorMapState).build(),
    ];

    const layersState: Signal<LayersState> = computed(() => {
      return {
        routeType: state.page.routeType(),
        layerEnabled: state.map.layers.layerEnabledMap(),
        zoom: state.map.zoom(),
      };
    });

    effect(() => {
      this.updateLayerVisibility(layersState());
    });
  }

  routeLayerChanged(): void {
    this.all.forEach((mapLayer) => {
      if (mapLayer.layerType === 'route' && mapLayer.layer.getVisible()) {
        mapLayer.layer.changed();
      }
    });
  }

  monitorLayerChanged(): void {
    this.all.forEach((mapLayer) => {
      if (mapLayer.layerType === 'monitor' && mapLayer.layer.getVisible()) {
        mapLayer.layer.changed();
      }
    });
  }

  private updateLayerVisibility(layersState: LayersState): void {
    this.all.forEach((mapLayer) => {
      const zoomInRange =
        layersState.zoom >= mapLayer.minZoom && layersState.zoom <= mapLayer.maxZoom;
      const layerEnabled = layersState.layerEnabled.get(mapLayer.layerType);
      const routeTypeMatch = !mapLayer.routeType || mapLayer.routeType === layersState.routeType;
      const visible = zoomInRange && layerEnabled && routeTypeMatch;
      mapLayer.layer.setVisible(visible);
    });
  }
}
