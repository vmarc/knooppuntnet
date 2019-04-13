import Coordinate from 'ol/coordinate';
import {PlanNode} from "../plan/plan-node";

export class PlannerDragFlag {

  constructor(public readonly flagType,
              public readonly legFeatureId,
              public readonly anchor1: Coordinate,
              public readonly anchor2: Coordinate,
              public readonly oldNode: PlanNode) {
  }

}
