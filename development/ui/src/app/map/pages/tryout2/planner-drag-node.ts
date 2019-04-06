import Coordinate from 'ol/coordinate';
import {PlanNode} from "./plan/plan-node";

export class PlannerDragNode {

  constructor(public readonly legNodeFeatureId,
              public readonly anchor1: Coordinate,
              public readonly anchor2: Coordinate,
              public readonly oldNode: PlanNode) {
  }

}
