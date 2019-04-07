import {Injectable} from '@angular/core';
import Map from 'ol/Map';
import {AppService} from "../app.service";
import {PlannerCommandStack} from "./planner/commands/planner-command-stack";
import {PlannerCommandStackImpl} from "./planner/commands/planner-command-stack-impl";
import {PlannerContext} from "./planner/interaction/planner-context";
import {PlannerContextImpl} from "./planner/interaction/planner-context-impl";
import {PlannerCrosshairLayer} from "./planner/interaction/planner-crosshair-layer";
import {PlannerElasticBandLayer} from "./planner/interaction/planner-elastic-band-layer";
import {PlannerEngine} from "./planner/interaction/planner-engine";
import {PlannerEngineImpl} from "./planner/interaction/planner-engine-impl";
import {PlannerRouteLayer} from "./planner/interaction/planner-route-layer";

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

  init(map: Map): void {
    this.crosshairLayer.addToMap(map);
    this.routeLayer.addToMap(map);
    this.elasticBandLayer.addToMap(map);
  }

}
