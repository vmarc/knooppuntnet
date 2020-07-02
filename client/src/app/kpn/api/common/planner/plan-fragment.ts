// this class is generated, please do not modify

import {LatLonImpl} from "../lat-lon-impl";
import {Coordinate} from "ol/coordinate";

export class PlanFragment {

  constructor(readonly meters: number,
              readonly orientation: number,
              readonly streetIndex: number,
              readonly coordinate: Coordinate,
              readonly latLon: LatLonImpl) {
  }

  public static fromJSON(jsonObject: any): PlanFragment {
    if (!jsonObject) {
      return undefined;
    }
    return new PlanFragment(
      jsonObject.meters,
      jsonObject.orientation,
      jsonObject.streetIndex,
      [jsonObject.coordinate.x, jsonObject.coordinate.y],
      LatLonImpl.fromJSON(jsonObject.latLon)
    );
  }
}
