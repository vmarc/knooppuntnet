import {List} from 'immutable';
import BaseLayer from 'ol/layer/Base';
import Map from 'ol/Map';
import {MapLayer} from './map-layer';

export class MapLayers {

  constructor(public layers: List<MapLayer>) {
  }

  applyMap(map: Map) {
    this.layers.forEach(mapLayer => {
      if (mapLayer.applyMap) {
        mapLayer.applyMap(map);
      }
    });
  }

  toArray(): Array<BaseLayer> {
    return this.layers.map(layer => layer.layer).toArray();
  }

}
