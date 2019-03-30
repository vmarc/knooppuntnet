import Coordinate from 'ol/View';
import {PlanNode} from "./plan-node";

export class PlannerNodeDrag {

  constructor(public readonly anchor1: Coordinate,
              public readonly anchor2: Coordinate,
              public readonly oldNode: PlanNode) {
  }

}
