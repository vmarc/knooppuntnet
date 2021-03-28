import {FlagFeature} from '../features/flag-feature';
import {Plan} from '../plan/plan';
import {PlanFlagType} from '../plan/plan-flag-type';
import {PlannerDragFlag} from './planner-drag-flag';

export class PlannerDragFlagAnalyzer {

  constructor(private plan: Plan) {
  }

  dragStarted(flag: FlagFeature): PlannerDragFlag {

    if (flag.flagType === PlanFlagType.start) {
      const sourceNode = this.plan.sourceNode;
      const sourceFlag = this.plan.sourceFlag;
      const anchor = sourceNode.coordinate;
      return new PlannerDragFlag(sourceFlag, null, anchor, anchor, sourceNode);
    }

    if (flag.flagType === PlanFlagType.end) {
      const sinkNode = this.plan.sinkNode();
      const sinkFlag = this.plan.sinkFlag();
      const anchor = sinkNode.coordinate;
      return new PlannerDragFlag(sinkFlag, null, anchor, anchor, sinkNode);
    }

    const legs = this.plan.legs;
    if (legs.isEmpty()) {
      return null;
    }

    const legIndex = legs.findIndex(leg => flag.id === leg.sinkFlag?.featureId);
    if (legIndex >= 0) {
      const previousLeg = legs.get(legIndex);
      const nextLeg = legs.get(legIndex + 1);
      return new PlannerDragFlag(
        previousLeg.sinkFlag,
        nextLeg.featureId,
        previousLeg.sourceNode.coordinate,
        nextLeg.sinkNode.coordinate,
        nextLeg.sourceNode
      );
    }

    return null;
  }

}
