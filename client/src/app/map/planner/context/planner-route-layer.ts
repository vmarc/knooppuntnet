import {Coordinate} from "ol/coordinate";
import {Plan} from "../plan/plan";
import {PlanFlag} from "../plan/plan-flag";
import {PlanLeg} from "../plan/plan-leg";

export interface PlannerRouteLayer {

  addFlag(flag: PlanFlag): void;

  removeFlag(featureId: string): void;

  updateFlagCoordinate(featureId: string, coordinate: Coordinate): void;

  addRouteLeg(leg: PlanLeg): void;

  removeRouteLeg(legId: string): void;

  removePlan(plan: Plan): void;

  addPlan(plan: Plan): void;
}
