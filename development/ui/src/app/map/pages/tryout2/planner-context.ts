import {PlannerRouteLayer} from "./planner-route-layer";
import {PlannerCrosshairLayer} from "./planner-crosshair-layer";
import {Observable} from "rxjs";
import {PlannerMode} from "./planner-mode";
import {List} from "immutable";
import {PlannerCommandStack} from "./commands/planner-command-stack";
import {Plan} from "./plan/plan";
import {PlanLegCache} from "./plan/plan-leg-cache";
import {PlanLegFragment} from "./plan/plan-leg-fragment";

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
