import {Coordinate} from "ol/coordinate";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerRouteLayer} from "./planner-route-layer";

export abstract class PlannerRouteLayerBase implements PlannerRouteLayer {

  abstract addFlag(flag: PlanFlag): void;

  abstract removeFlag(featureId: string): void;

  abstract updateFlagCoordinate(featureId: string, coordinate: Coordinate): void;

  abstract addRouteLeg(leg: PlanLeg): void;

  abstract removeRouteLeg(legId: string): void;

  removePlan(plan: Plan): void {
    if (plan.source) {
      this.removeFlag(plan.source.featureId);
    }
    plan.legs.forEach(leg => {
      this.removeRouteLeg(leg.featureId);
      this.removeFlag(leg.sink.featureId);
    });
  }

  addPlan(plan: Plan): void {
    if (plan.source) {
      this.addFlag(PlanFlag.fromStartNode(plan.source));
    }
    for (let i = 0; i < plan.legs.size; i++) {
      const leg = plan.legs.get(i);
      this.addRouteLeg(leg);
      if (i < plan.legs.size - 1) {
        this.addFlag(PlanFlag.fromViaNode(leg.sink));
      } else {
        this.addFlag(PlanFlag.fromEndNode(leg.sink));
      }
    }
  }
}
