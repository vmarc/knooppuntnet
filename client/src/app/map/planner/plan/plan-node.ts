import Coordinate from "ol/coordinate";
import {Util} from "../../../components/shared/util";
import {LatLonImpl} from "../../../kpn/shared/lat-lon-impl";
import {FeatureId} from "../features/feature-id";

export class PlanNode {

  private constructor(readonly featureId: string,
                      readonly nodeId: string,
                      readonly nodeName: string,
                      readonly coordinate: Coordinate,
                      readonly latLon: LatLonImpl) {
  }

  static create(nodeId: string, nodeName: string, latLon: LatLonImpl): PlanNode {
    const coordinate = Util.latLonToCoordinate(latLon);
    return new PlanNode(FeatureId.next(), nodeId, nodeName, coordinate, latLon);
  }

  static withCoordinate(nodeId: string, nodeName: string, coordinate: Coordinate): PlanNode {
    const latLon = Util.latLonFromCoordinate(coordinate);
    return new PlanNode(FeatureId.next(), nodeId, nodeName, coordinate, latLon);
  }

}
