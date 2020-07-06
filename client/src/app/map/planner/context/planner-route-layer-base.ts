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
    this.removeFlag(plan.sourceFlag);
    plan.legs.forEach(leg => {
      this.removePlanLeg(leg.featureId);
      this.removeFlag(leg.sinkFlag);
      this.removeFlag(leg.viaFlag);
    });
  }

  addPlan(plan: Plan): void {
    this.addFlag(plan.sourceFlag);
    for (let i = 0; i < plan.legs.size; i++) {
      const leg = plan.legs.get(i);
      this.addPlanLeg(leg);
      this.addFlag(leg.sinkFlag);
      this.addFlag(leg.viaFlag);
    }
  }
}
