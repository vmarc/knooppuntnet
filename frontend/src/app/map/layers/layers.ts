import { Signal } from '@angular/core';
import { NetworkType } from '@api/custom';
import { MapboxVectorLayer } from 'ol-mapbox-style';
import TileLayer from 'ol/layer/Tile';
import VectorTileLayer from 'ol/layer/VectorTile';
import OSM from 'ol/source/OSM';
import TileDebug from 'ol/source/TileDebug';
import { MapStyleOptions } from '../../state/map-style-options';
import { BackgroundLayer } from './background-layer';
import { Grid256Layer } from './grid-256-layer';
import { Grid512Layer } from './grid-512-layer';
import { OsmLayer } from './osm-layer';
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

  readonly osmLayer: TileLayer<OSM>;
  readonly backgroundLayer: MapboxVectorLayer;
  readonly grid256Layer: TileLayer<TileDebug>;
  readonly grid512Layer: TileLayer<TileDebug>;
  readonly routeLayer: VectorTileLayer;

  constructor(styleOptions: Signal<MapStyleOptions>) {
    this.osmLayer = OsmLayer.build();
    this.backgroundLayer = BackgroundLayer.build();
    this.grid256Layer = Grid256Layer.build();
    this.grid512Layer = Grid512Layer.build();
    this.routeLayer = new RouteLayer(styleOptions).build(NetworkType.hiking);
  }
}
