import {Coordinate} from "ol/coordinate";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerRouteLayer} from "./planner-route-layer";

export abstract class PlannerRouteLayerBase implements PlannerRouteLayer {

  abstract addFlag(flag: PlanFlag): void;

  abstract updateFlag(flag: PlanFlag): void;

  abstract removeFlag(flag: PlanFlag): void;

  abstract removeFlagWithFeatureId(featureId: string): void;

  abstract updateFlagCoordinate(featureId: string, coordinate: Coordinate): void;

  abstract addPlanLeg(leg: PlanLeg): void;

  abstract removePlanLeg(legId: string): void;

  removePlan(plan: Plan): void {
    if (plan.sourceNode) {
      this.removeFlagWithFeatureId(plan.sourceNode.featureId);
    }
    plan.legs.forEach(leg => {
      this.removePlanLeg(leg.featureId);
      this.removeFlagWithFeatureId(leg.sinkFlag.featureId);
      if (leg.viaFlag !== null) {
        this.removeFlagWithFeatureId(leg.viaFlag.featureId);
      }
    });
  }

  addPlan(plan: Plan): void {
    if (plan.sourceNode) {
      this.addFlag(PlanFlag.oldStart(plan.sourceNode));
    }
    for (let i = 0; i < plan.legs.size; i++) {
      const leg = plan.legs.get(i);
      this.addPlanLeg(leg);
      if (leg.sinkFlag !== null) {
        this.addFlag(leg.sinkFlag);
      }
      if (leg.viaFlag !== null) {
        this.addFlag(leg.viaFlag);
      }
    }
  }
}
