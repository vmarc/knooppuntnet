import {Injectable} from '@angular/core';
import Map from 'ol/Map';
import {AppService} from "../app.service";
import {PlannerCommandStack} from "./planner/commands/planner-command-stack";
import {PlannerCommandStackImpl} from "./planner/commands/planner-command-stack-impl";
import {PlannerContext} from "./planner/context/planner-context";
import {PlannerCrosshairImpl} from "./planner/context/planner-crosshair-impl";
import {PlannerElasticBandImpl} from "./planner/context/planner-elastic-band-impl";
import {PlannerRouteLayerImpl} from "./planner/context/planner-route-layer-impl";
import {PlannerEngine} from "./planner/interaction/planner-engine";
import {PlannerEngineImpl} from "./planner/interaction/planner-engine-impl";
import {PlanLegCache} from "./planner/plan/plan-leg-cache";

@Injectable({
  providedIn: 'root'
})
export class PlannerService {

  constructor(private appService: AppService) {
  }

  private commandStack: PlannerCommandStack = new PlannerCommandStackImpl();
  private routeLayer = new PlannerRouteLayerImpl();
  private crosshairLayer = new PlannerCrosshairImpl();
  private elasticBandLayer = new PlannerElasticBandImpl();
  private legCache: PlanLegCache = new PlanLegCache();

  context: PlannerContext = new PlannerContext(
    this.commandStack,
    this.routeLayer,
    this.crosshairLayer,
    this.elasticBandLayer,
    this.legCache
  );

  engine: PlannerEngine = new PlannerEngineImpl(this.context, this.appService);

  init(map: Map): void {
    this.crosshairLayer.addToMap(map);
    this.routeLayer.addToMap(map);
    this.elasticBandLayer.addToMap(map);
  }

}
