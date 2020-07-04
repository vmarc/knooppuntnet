import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {FlagFeature} from "../features/flag-feature";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlannerDragFlag} from "./planner-drag-flag";

export class PlannerDragFlagAnalyzer {

  constructor(private plan: Plan) {
  }

  dragStarted(flag: FlagFeature): PlannerDragFlag {

    if (flag.flagType === PlanFlagType.Start) {
      const sourceNode = this.plan.sourceNode;
      const anchor = sourceNode.coordinate;
      return new PlannerDragFlag(PlanFlagType.Start, null, anchor, anchor, sourceNode);
    }

    if (flag.flagType === PlanFlagType.End) {
      const sinkNode = this.plan.sinkNode();
      const anchor = sinkNode.coordinate;
      return new PlannerDragFlag(PlanFlagType.End, null, anchor, anchor, sinkNode);
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
