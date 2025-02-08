import { computed } from '@angular/core';
import { effect } from '@angular/core';
import { Signal } from '@angular/core';
import { State } from '@app/state/state';
import { LayersState } from '../../state/layers-state';
import { MapStyleOptions } from '../../state/map-style-options';
import { PoiStyleMap } from '../style/poi-style-map';
import { OpendataBitmapTileLayer } from './opendata-bitmap-tile-layer';
import { OpendataVectorTileLayer } from './opendata-vector-tile-layer';
import { StandardBackground } from './standard-background';
import { GridLayer } from './grid-layer';
import { MapLayer } from './map-layer';
import { OsmBackgroundLayer } from './osm-background-layer';
import { PoiLayer } from './poi-layer';
import { RouteLayer } from './route-layer';

export class Layers {
  static readonly zIndexRouteLayer = 60;
  static readonly zIndexPoiLayer = 40;

  readonly poiLayer: MapLayer;
  readonly all: ReadonlyArray<MapLayer>;

  constructor(
    state: State,
    styleOptions: Signal<MapStyleOptions>,
    poiStyleMap: Signal<PoiStyleMap>,
    poiActive: Signal<ReadonlyMap<string, boolean>>
  ) {
    this.poiLayer = PoiLayer.build(poiStyleMap, poiActive);
    this.all = [
      StandardBackground.build(),
      OsmBackgroundLayer.build(),
      GridLayer.build(),
      new RouteLayer(styleOptions).build('cycling'),
      new RouteLayer(styleOptions).build('hiking'),
      new RouteLayer(styleOptions).build('horse-riding'),
      new RouteLayer(styleOptions).build('motorboat'),
      new RouteLayer(styleOptions).build('canoe'),
      new RouteLayer(styleOptions).build('inline-skating'),
      this.poiLayer,
      OpendataBitmapTileLayer.build('flanders-open-data', 'hiking', 'flanders/hiking'),
      OpendataVectorTileLayer.build('flanders-open-data', 'hiking', 'flanders/hiking'),
      OpendataBitmapTileLayer.build('flanders-open-data', 'cycling', 'flanders/cycling'),
      OpendataVectorTileLayer.build('flanders-open-data', 'cycling', 'flanders/cycling'),
      OpendataBitmapTileLayer.build('netherlands-open-data', 'hiking', 'netherlands/hiking'),
      OpendataVectorTileLayer.build('netherlands-open-data', 'hiking', 'netherlands/hiking'),
      OpendataBitmapTileLayer.build('netherlands-open-data', 'cycling', 'netherlands/cycling'),
      OpendataVectorTileLayer.build('netherlands-open-data', 'cycling', 'netherlands/cycling'),
      OpendataBitmapTileLayer.build('france-open-data', 'hiking', 'france/hiking'),
      OpendataVectorTileLayer.build('france-open-data', 'hiking', 'france/hiking'),
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
