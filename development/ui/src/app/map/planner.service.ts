import {Injectable} from '@angular/core';
import Map from 'ol/Map';
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
import {PlannerElasticBandLayer} from "./pages/tryout2/planner-elastic-band-layer";

@Injectable({
  providedIn: 'root'
})
export class PlannerService {

  constructor(private appService: AppService) {
  }

  private commandStack: PlannerCommandStack = new PlannerCommandStackImpl();
  private routeLayer = new PlannerRouteLayer();
  private crosshairLayer = new PlannerCrosshairLayer();
  private elasticBandLayer = new PlannerElasticBandLayer();

  context: PlannerContext = new PlannerContextImpl(
    this.commandStack,
    this.routeLayer,
    this.crosshairLayer,
    this.elasticBandLayer
  );

  engine: PlannerEngine = new PlannerEngineImpl(this.context, this.appService);

  private simulationPlan = new TestRouteData().buildTestPlan(this.context);

  init(map: Map): void {
    this.crosshairLayer.addToMap(map);
    this.routeLayer.addToMap(map);
    this.elasticBandLayer.addToMap(map);
  }

}
