import Coordinate from "ol/coordinate";
import {PlanNode} from "../plan/plan-node";

export class PlannerDragFlag {

  constructor(readonly flagType,
              readonly legFeatureId,
              readonly anchor1: Coordinate,
              readonly anchor2: Coordinate,
              readonly oldNode: PlanNode) {
  }

}
