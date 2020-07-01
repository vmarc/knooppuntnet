import {Coordinate} from "ol/coordinate";
import {LatLonImpl} from "../lat-lon-impl";

export class PlanFragment {

  constructor(readonly meters: number,
              readonly orientation: number,
              readonly streetIndex: number,
              readonly coordinate: Coordinate,
              readonly latLon: LatLonImpl) {
  }

}
