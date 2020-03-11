import {Coordinate} from "ol/coordinate";
import {MapFeature} from "./map-feature";

export class PoiFeature extends MapFeature {

  constructor(readonly poiId: string,
              readonly poiType: string,
              readonly layer: string,
              readonly coordinate: Coordinate) {
    super();
  }

}
