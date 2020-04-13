import BaseLayer from "ol/layer/Base";
import Map from "ol/Map";

export class MapLayer {

  constructor(public layer: BaseLayer,
              public applyMap?: (map: Map) => void) {
  }

}
