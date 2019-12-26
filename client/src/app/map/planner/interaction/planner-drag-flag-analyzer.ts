import {Plan} from "../plan/plan";
import {PlanFlagType} from "../plan/plan-flag-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerDragFlag} from "./planner-drag-flag";
import {FlagFeature} from "../features/flag-feature";

export class PlannerDragFlagAnalyzer {

  constructor(private plan: Plan) {
  }

  dragStarted(flag: FlagFeature): PlannerDragFlag {

    if (flag.flagType == PlanFlagType.Start) {
      const anchor = this.plan.source.coordinate;
      return new PlannerDragFlag(PlanFlagType.Start, null, anchor, anchor, this.plan.source);
    }

    const legs = this.plan.legs;
    if (legs.isEmpty()) {
      return null;
    }

    const lastLeg: PlanLeg = legs.last();
    if (lastLeg.sink.featureId == flag.id) {
      const anchor = lastLeg.sink.coordinate;
      return new PlannerDragFlag(PlanFlagType.Via, lastLeg.featureId, anchor, anchor, lastLeg.sink);
    }

    const legIndex = legs.findIndex(leg => leg.source.featureId === flag.id);
    if (legIndex > 0) {
      const previousLeg = legs.get(legIndex - 1);
      const nextLeg = legs.get(legIndex);
      return new PlannerDragFlag(
        PlanFlagType.Via,
        nextLeg.featureId,
        previousLeg.source.coordinate,
        nextLeg.sink.coordinate,
        nextLeg.source
      );
    }

    return null;
  }

}
