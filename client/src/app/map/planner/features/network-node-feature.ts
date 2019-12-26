import Coordinate from "ol/coordinate";
import {PlanNode} from "../plan/plan-node";
import {MapFeature} from "./map-feature";

export class NetworkNodeFeature extends MapFeature {

  constructor(readonly node: PlanNode) {
    super();
  }

  static create(nodeId: string, nodeName: string, coordinate: Coordinate) {
    return new NetworkNodeFeature(PlanNode.withCoordinate(nodeId, nodeName, coordinate));
  }

}
