import {Injectable} from '@angular/core';
import {PlannerContextImpl} from "./pages/tryout2/planner-context-impl";
import {PlannerContext} from "./pages/tryout2/planner-context";
import {PlannerRouteLayer} from "./pages/tryout2/planner-route-layer";
import {PlannerCrosshairLayer} from "./pages/tryout2/planner-crosshair-layer";
import {PlannerEngineImpl} from "./pages/tryout2/planner-engine-impl";
import {PlannerEngine} from "./pages/tryout2/planner-engine";
import {TestRouteData} from "./pages/tryout2/test-route-data";
import {AppService} from "../app.service";
import {PlannerCommandStack} from "./pages/tryout2/commands/planner-command-stack";
import {PlannerCommandStackImpl} from "./pages/tryout2/commands/planner-command-stack-impl";

@Injectable({
  providedIn: 'root'
})
export class PlannerService {

  constructor(private appService: AppService) {
  }

  private commandStack: PlannerCommandStack = new PlannerCommandStackImpl();
  private routeLayer = new PlannerRouteLayer();
  private crosshairLayer = new PlannerCrosshairLayer();

  context: PlannerContext = new PlannerContextImpl(this.commandStack, this.routeLayer, this.crosshairLayer);

  engine: PlannerEngine = new PlannerEngineImpl(this.context, this.appService);

  private simulationPlan = new TestRouteData().buildTestPlan(this.context);

}
