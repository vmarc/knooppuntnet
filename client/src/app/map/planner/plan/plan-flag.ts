import {Coordinate} from "ol/coordinate";
import {PlanFlagType} from "./plan-flag-type";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";

export class PlanFlag {

  constructor(readonly flagType: PlanFlagType,
              readonly featureId: string,
              readonly coordinate: Coordinate) {
  }

  static fromStartNode(node: PlanNode): PlanFlag {
    return new PlanFlag(PlanFlagType.Start, node.featureId, node.coordinate);
  }

  static fromEndNode(node: PlanNode): PlanFlag {
    return new PlanFlag(PlanFlagType.End, node.featureId, node.coordinate);
  }

  static fromViaNode(node: PlanNode): PlanFlag {
    return new PlanFlag(PlanFlagType.Via, node.featureId, node.coordinate);
  }

}
