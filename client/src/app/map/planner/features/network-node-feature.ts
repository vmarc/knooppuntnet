import {Coordinate} from "ol/coordinate";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {PlanUtil} from "../plan/plan-util";
import {MapFeature} from "./map-feature";

export class NetworkNodeFeature extends MapFeature {

  constructor(readonly node: PlanNode) {
    super();
  }

  static create(nodeId: string, nodeName: string, coordinate: Coordinate) {
    return new NetworkNodeFeature(PlanUtil.planNodeWithCoordinate(nodeId, nodeName, coordinate));
  }

}
