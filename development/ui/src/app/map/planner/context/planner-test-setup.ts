import {PlannerCommandStack} from "../commands/planner-command-stack";
import {PlanLegCache} from "../plan/plan-leg-cache";
import {PlannerContext} from "./planner-context";
import {PlannerCrosshairMock} from "./planner-crosshair-mock";
import {PlannerElasticBandMock} from "./planner-elastic-band-mock";
import {PlannerRouteLayerMock} from "./planner-route-layer-mock";

export class PlannerTestSetup {
  readonly commandStack = new PlannerCommandStack();
  readonly routeLayer = new PlannerRouteLayerMock();
  readonly crosshair = new PlannerCrosshairMock();
  readonly elasticBand = new PlannerElasticBandMock();
  readonly legs = new PlanLegCache();
  readonly context = new PlannerContext(this.commandStack, this.routeLayer, this.crosshair, this.elasticBand, this.legs);
}
