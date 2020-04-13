import {List} from "immutable";
import Map from "ol/Map";
import {MapLayer} from "./map-layer";

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

}
