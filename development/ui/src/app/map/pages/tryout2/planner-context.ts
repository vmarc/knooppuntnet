import {PlannerCommandStack} from "./planner-command-stack";
import {PlannerRouteLayer} from "./planner-route-layer";
import {PlannerCrosshairLayer} from "./planner-crosshair-layer";
import {Observable} from "rxjs";
import {PlannerMode} from "./planner-mode";
import {Plan} from "./plan";

export interface PlannerContext {
  commandStack: PlannerCommandStack;
  routeLayer: PlannerRouteLayer;
  crosshairLayer: PlannerCrosshairLayer;
  mode: Observable<PlannerMode>;
  plan: Observable<Plan>;
}
