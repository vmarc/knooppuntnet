import { computed } from '@angular/core';
import { effect } from '@angular/core';
import { Signal } from '@angular/core';
import { State } from '@app/state';
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
  static readonly zIndexOsmLayer = 90;
  static readonly zIndexGpxLayer = 80;
  static readonly zIndexPlannerMarkerLayer = 70;
  static readonly zIndexNetworkNodesLayer = 65;
  static readonly zIndexNetworkLayer = 60;
  static readonly zIndexPlannerRouteLayer = 50;
  static readonly zIndexPoiLayer = 40;
  static readonly zIndexHighlightLayer = 30;

  readonly standardBackgroundLayer: MapLayer;
  readonly osmBackgroundLayer: MapLayer;
  readonly gridLayer: MapLayer;
  readonly routeLayer: MapLayer;
  readonly poiLayer: MapLayer;
  readonly all: ReadonlyArray<MapLayer>;

  constructor(
    state: State,
    styleOptions: Signal<MapStyleOptions>,
    poiStyleMap: Signal<PoiStyleMap>,
    poiActive: Signal<ReadonlyMap<string, boolean>>
  ) {
    this.standardBackgroundLayer = StandardBackground.build();
    this.osmBackgroundLayer = OsmBackgroundLayer.build();
    this.gridLayer = GridLayer.build();
    this.routeLayer = new RouteLayer(styleOptions).build('hiking');
    this.poiLayer = PoiLayer.build(poiStyleMap, poiActive);
    this.all = [
      this.osmBackgroundLayer,
      this.standardBackgroundLayer,
      this.gridLayer,
      this.routeLayer,
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
        networkType: state.page.networkType(),
        layerEnabled: state.map.layerEnabled(),
        zoom: state.map.zoom(),
      };
    });

    effect(() => {
      this.updateLayerVisibility(layersState());
    });
  }

  private updateLayerVisibility(layersState: LayersState): void {
    this.all.forEach((mapLayer) => {
      const zoomInRange =
        layersState.zoom >= mapLayer.minZoom && layersState.zoom <= mapLayer.maxZoom;
      const layerEnabled = layersState.layerEnabled.get(mapLayer.layerType);
      const networkTypeMatch =
        !mapLayer.networkType || mapLayer.networkType === layersState.networkType;
      const visible = zoomInRange && layerEnabled && networkTypeMatch;
      mapLayer.layer.setVisible(visible);
    });
  }
}
