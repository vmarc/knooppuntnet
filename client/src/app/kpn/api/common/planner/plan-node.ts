import {Coordinate} from "ol/coordinate";
import {LatLonImpl} from "../lat-lon-impl";

export class PlanNode {

  constructor(readonly featureId: string,
              readonly nodeId: string,
              readonly nodeName: string,
              readonly coordinate: Coordinate,
              readonly latLon: LatLonImpl) {
  }

}
