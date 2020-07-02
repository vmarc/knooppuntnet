import {Coordinate} from "ol/coordinate";
import {Plan} from "../../../kpn/api/common/planner/plan";
import {PlanLeg} from "../../../kpn/api/common/planner/plan-leg";
import {PlanFlag} from "../plan/plan-flag";

export interface PlannerRouteLayer {

  addFlag(flag: PlanFlag): void;

  removeFlag(featureId: string): void;

  updateFlagCoordinate(featureId: string, coordinate: Coordinate): void;

  addPlanLeg(leg: PlanLeg): void;

  removePlanLeg(legId: string): void;

  removePlan(plan: Plan): void;

  addPlan(plan: Plan): void;
}
