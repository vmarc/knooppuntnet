import {Coordinate} from "ol/coordinate";
import {PlanNode} from "../../../kpn/api/common/planner/plan-node";
import {PlanFlagType} from "../plan/plan-flag-type";

export class PlannerDragFlag {

  constructor(readonly flagType: PlanFlagType,
              readonly legFeatureId: string,
              readonly anchor1: Coordinate,
              readonly anchor2: Coordinate,
              readonly oldNode: PlanNode) {
  }

}
