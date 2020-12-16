import {PlanNode} from '@api/common/planner/plan-node';
import {Coordinate} from 'ol/coordinate';
import {PlanFlag} from '../plan/plan-flag';

export class PlannerDragFlag {

  constructor(readonly planFlag: PlanFlag,
              readonly legFeatureId: string,
              readonly anchor1: Coordinate,
              readonly anchor2: Coordinate,
              readonly oldNode: PlanNode) {
  }

}
