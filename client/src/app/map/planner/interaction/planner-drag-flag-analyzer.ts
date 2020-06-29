import {Plan} from "../plan/plan";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerDragFlag} from "./planner-drag-flag";
import {FlagFeature} from "../features/flag-feature";

export class PlannerDragFlagAnalyzer {

  constructor(private plan: Plan) {
  }

  dragStarted(flag: FlagFeature): PlannerDragFlag {

    if (flag.flagType === PlanFlagType.Start) {
      const anchor = this.plan.sourceNode.coordinate;
      return new PlannerDragFlag(PlanFlagType.Start, null, anchor, anchor, this.plan.sourceNode);
    }

    const legs = this.plan.legs;
    if (legs.isEmpty()) {
      return null;
    }

    const lastLeg: PlanLeg = legs.last();
    if (lastLeg.sinkNode.featureId === flag.id) {
      const anchor = lastLeg.sinkNode.coordinate;
      return new PlannerDragFlag(PlanFlagType.Via, lastLeg.featureId, anchor, anchor, lastLeg.sinkNode);
    }

    const legIndex = legs.findIndex(leg => leg.sourceNode.featureId === flag.id);
    if (legIndex > 0) {
      const previousLeg = legs.get(legIndex - 1);
      const nextLeg = legs.get(legIndex);
      return new PlannerDragFlag(
        PlanFlagType.Via,
        nextLeg.featureId,
        previousLeg.sourceNode.coordinate,
        nextLeg.sinkNode.coordinate,
        nextLeg.sourceNode
      );
    }

    return null;
  }

}
