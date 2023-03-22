import { List } from 'immutable';
import BaseLayer from 'ol/layer/Base';
import { MapLayer } from './map-layer';

export class MapLayers {
  constructor(public layers: List<MapLayer>) {}

  updateSize(): void {
    this.layers.forEach((mapLayer) => {
      mapLayer.updateSize();
    });
  }

  toArray(): Array<BaseLayer> {
    return this.layers.map((layer) => layer.layer).toArray();
  }
}
