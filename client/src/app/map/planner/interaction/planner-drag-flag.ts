import {Coordinate} from "ol/coordinate";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanNode} from "../plan/plan-node";

export class PlannerDragFlag {

  constructor(readonly flagType: PlanFlagType,
              readonly legFeatureId: string,
              readonly anchor1: Coordinate,
              readonly anchor2: Coordinate,
              readonly oldNode: PlanNode) {
  }

}
