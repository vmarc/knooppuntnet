import {Coordinate} from "ol/coordinate";
import {Plan} from "../plan/plan";
import {PlanLeg} from "../plan/plan-leg";
import {PlanFlag} from "../plan/plan-flag";

export interface PlannerRouteLayer {

  addFlag(flag: PlanFlag): void;

  removeFlag(flag: PlanFlag): void;

  removeFlagWithFeatureId(featureId: string): void;

  updateFlag(flag: PlanFlag): void;

  updateFlagCoordinate(featureId: string, coordinate: Coordinate): void;

  addPlanLeg(leg: PlanLeg): void;

  removePlanLeg(legId: string): void;

  removePlan(plan: Plan): void;

  addPlan(plan: Plan): void;
}
