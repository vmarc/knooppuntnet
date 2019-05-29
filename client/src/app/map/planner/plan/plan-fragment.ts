import Coordinate from "ol/coordinate";
import {LatLonImpl} from "../../../kpn/shared/lat-lon-impl";

export class PlanFragment {

  constructor(readonly meters: number,
              readonly orientation: number,
              readonly streetIndex: number,
              readonly coordinate: Coordinate,
              readonly latLon: LatLonImpl) {
  }

}
