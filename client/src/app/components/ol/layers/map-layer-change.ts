import { MapLayer } from './map-layer';

export class MapLayerChange {
  constructor(public oldLayer: MapLayer, public newLayer: MapLayer) {}
}
