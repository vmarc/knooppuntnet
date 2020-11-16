import {FlagFeature} from '../features/flag-feature';
import {Plan} from '../plan/plan';
import {PlanFlagType} from '../plan/plan-flag-type';
import {PlannerDragFlag} from './planner-drag-flag';
import {PlannerDragViaRouteFlag} from './planner-drag-via-route-flag';

export class PlannerDragViaRouteFlagAnalyzer {

  constructor(private plan: Plan) {
  }

  dragStarted(flag: FlagFeature): PlannerDragFlag {
    if (flag.flagType === PlanFlagType.Via) {
      const leg = this.plan.legs.find(leg => leg.viaFlag?.featureId === flag.id);
      if (leg) {
        return new PlannerDragViaRouteFlag(
          leg.viaFlag,
          leg.featureId,
          leg.sourceNode.coordinate,
          leg.sinkNode.coordinate,
          leg.sourceNode
        );
      }
    }
    return null;
  }

}
