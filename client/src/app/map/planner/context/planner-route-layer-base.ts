import {Coordinate} from "ol/coordinate";
import {Plan} from "../../../kpn/api/common/planner/plan";
import {PlanLeg} from "../../../kpn/api/common/planner/plan-leg";
import {PlanFlag} from "../plan/plan-flag";
import {PlannerRouteLayer} from "./planner-route-layer";

export abstract class PlannerRouteLayerBase implements PlannerRouteLayer {

  abstract addFlag(flag: PlanFlag): void;

  abstract removeFlag(featureId: string): void;

  abstract updateFlagCoordinate(featureId: string, coordinate: Coordinate): void;

  abstract addRouteLeg(leg: PlanLeg): void;

  abstract removeRouteLeg(legId: string): void;

  removePlan(plan: Plan): void {
    if (plan.sourceNode) {
      this.removeFlag(plan.sourceNode.featureId);
    }
    plan.legs.forEach(leg => {
      this.removeRouteLeg(leg.featureId);
      this.removeFlag(leg.sinkNode.featureId);
    });
  }

  addPlan(plan: Plan): void {
    if (plan.sourceNode) {
      this.addFlag(PlanFlag.fromStartNode(plan.sourceNode));
    }
    for (let i = 0; i < plan.legs.size; i++) {
      const leg = plan.legs.get(i);
      this.addRouteLeg(leg);
      if (i < plan.legs.size - 1) {
        this.addFlag(PlanFlag.fromViaNode(leg.sinkNode));
      } else {
        this.addFlag(PlanFlag.fromEndNode(leg.sinkNode));
      }
    }
  }
}
