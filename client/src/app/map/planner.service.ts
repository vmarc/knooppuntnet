import {Injectable} from "@angular/core";
import {Map as TranslationMap} from "immutable";
import Map from "ol/Map";
import {AppService} from "../app.service";
import {PlannerCommandStack} from "./planner/commands/planner-command-stack";
import {PlannerContext} from "./planner/context/planner-context";
import {PlannerCursorImpl} from "./planner/context/planner-cursor-impl";
import {PlannerElasticBandImpl} from "./planner/context/planner-elastic-band-impl";
import {PlannerLegRepositoryImpl} from "./planner/context/planner-leg-repository-impl";
import {PlannerRouteLayerImpl} from "./planner/context/planner-route-layer-impl";
import {PlannerEngine} from "./planner/interaction/planner-engine";
import {PlannerEngineImpl} from "./planner/interaction/planner-engine-impl";
import {PlanLegCache} from "./planner/plan/plan-leg-cache";
import {PlannerOverlayImpl} from "./planner/context/planner-overlay-impl";
import {MapService} from "../components/ol/map.service";

@Injectable({
  providedIn: "root"
})
export class PlannerService {

  private translations: TranslationMap<string, string> = null;
  private commandStack = new PlannerCommandStack();
  private routeLayer = new PlannerRouteLayerImpl();
  private cursor = new PlannerCursorImpl();
  private elasticBand = new PlannerElasticBandImpl();
  private legRepository = new PlannerLegRepositoryImpl(this.appService);
  private legCache: PlanLegCache = new PlanLegCache();
  private overlay = new PlannerOverlayImpl(this.mapService);
  context: PlannerContext = new PlannerContext(
    this.commandStack,
    this.routeLayer,
    this.cursor,
    this.elasticBand,
    this.legRepository,
    this.legCache,
    this.overlay
  );
  engine: PlannerEngine = new PlannerEngineImpl(this.context);

  constructor(private appService: AppService,
              private mapService: MapService) {
  }

  init(map: Map): void {
    this.cursor.addToMap(map);
    this.routeLayer.addToMap(map);
    this.elasticBand.addToMap(map);
    this.overlay.addToMap(map);
  }

  updateTranslationRegistry(translationElements: HTMLCollection) {
    if (this.translations === null) {
      const keysAndValues: Array<[string, string]> = [];
      Array.from(translationElements).forEach(span => {
        const id = span.getAttribute("id");
        const translation = span.textContent;
        keysAndValues.push([id, translation]);
      });
      this.translations = TranslationMap<string, string>(keysAndValues);
    }
  }

  isTranslationRegistryUpdated() {
    return this.translations != null;
  }

  translate(key: string): string {
    if (this.translations == null) {
      return "";
    }
    return this.translations.get(key);
  }

}
