import {PlannerCommandStack} from "../commands/planner-command-stack";
import {PlanLegCache} from "../plan/plan-leg-cache";
import {PlannerContext} from "./planner-context";
import {PlannerCrosshairMock} from "./planner-crosshair-mock";
import {PlannerCursorMock} from "./planner-cursor-mock";
import {PlannerElasticBandMock} from "./planner-elastic-band-mock";
import {PlannerLegRepositoryMock} from "./planner-leg-repository-mock";
import {PlannerRouteLayerMock} from "./planner-route-layer-mock";

export class PlannerTestSetup {
  readonly commandStack = new PlannerCommandStack();
  readonly routeLayer = new PlannerRouteLayerMock();
  readonly crosshair = new PlannerCrosshairMock();
  readonly cursor = new PlannerCursorMock();
  readonly elasticBand = new PlannerElasticBandMock();
  readonly legRepository = new PlannerLegRepositoryMock();
  readonly legs = new PlanLegCache();
  readonly context = new PlannerContext(
    this.commandStack,
    this.routeLayer,
    this.crosshair,
    this.cursor,
    this.elasticBand,
    this.legRepository,
    this.legs
  );
}
