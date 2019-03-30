import {PlannerCommandStack} from "./planner-command-stack";
import {PlannerRouteLayer} from "./planner-route-layer";
import {PlannerCrosshairLayer} from "./planner-crosshair-layer";
import {Observable} from "rxjs";
import {PlannerMode} from "./planner-mode";
import {Plan} from "./plan";
import {PlanLegFragment} from "./plan-leg-fragment";
import {List} from "immutable";
import {PlanLegCache} from "./plan-leg-cache";

export interface PlannerContext {
  commandStack: PlannerCommandStack;
  routeLayer: PlannerRouteLayer;
  crosshairLayer: PlannerCrosshairLayer;
  mode: Observable<PlannerMode>;
  planObserver: Observable<Plan>;
  plan: Plan;
  legCache: PlanLegCache;

  updatePlan(plan: Plan);

  updatePlanLeg(legId: string, fragments: List<PlanLegFragment>);

}
