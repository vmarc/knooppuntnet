import {Injectable} from "@angular/core";
import {List} from "immutable";
import {Map as TranslationMap} from "immutable";
import Map from "ol/Map";
import {BehaviorSubject} from "rxjs";
import {AppService} from "../app.service";
import {MapService} from "../components/ol/services/map.service";
import {PlannerContext} from "./planner/context/planner-context";
import {PlannerCursorImpl} from "./planner/context/planner-cursor-impl";
import {PlannerElasticBandImpl} from "./planner/context/planner-elastic-band-impl";
import {PlannerHighlightLayerImpl} from "./planner/context/planner-highlight-layer-impl";
import {PlannerLegRepositoryImpl} from "./planner/context/planner-leg-repository-impl";
import {PlannerMarkerLayerImpl} from "./planner/context/planner-marker-layer-impl";
import {PlannerOverlayImpl} from "./planner/context/planner-overlay-impl";
import {PlannerRouteLayerImpl} from "./planner/context/planner-route-layer-impl";
import {PlannerEngine} from "./planner/interaction/planner-engine";
import {PlannerEngineImpl} from "./planner/interaction/planner-engine-impl";
import {PlanLegCache} from "./planner/plan/plan-leg-cache";
import {PlanRoute} from "../kpn/api/common/planner/plan-route";
import {PlanUtil} from "./planner/plan/plan-util";

@Injectable({
  providedIn: "root"
})
export class PlannerService {

  engine: PlannerEngine;
  resultMode$ = new BehaviorSubject<string>("compact");
  private translations: TranslationMap<string, string> = null;
  private routeLayer = new PlannerRouteLayerImpl();
  private markerLayer = new PlannerMarkerLayerImpl();
  private cursor = new PlannerCursorImpl();
  private elasticBand = new PlannerElasticBandImpl();
  private highlightLayer = new PlannerHighlightLayerImpl();
  private legRepository = new PlannerLegRepositoryImpl(this.appService);
  private legCache: PlanLegCache = new PlanLegCache();
  private overlay = new PlannerOverlayImpl(this.mapService);
  context: PlannerContext = new PlannerContext(
    this.routeLayer,
    this.markerLayer,
    this.cursor,
    this.elasticBand,
    this.highlightLayer,
    this.legRepository,
    this.legCache,
    this.overlay
  );

  constructor(private appService: AppService,
              private mapService: MapService) {
    this.engine = new PlannerEngineImpl(this.context);
  }

  init(map: Map): void {
    this.cursor.addToMap(map);
    this.routeLayer.addToMap(map);
    this.markerLayer.addToMap(map);
    this.elasticBand.addToMap(map);
    this.highlightLayer.addToMap(map);
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

  hasColour(planRoute: PlanRoute): boolean {
    return planRoute.segments.filter(segment => !!segment.colour).size > 0;
  }

  colours(planRoute: PlanRoute): string {
    const colourValues = planRoute.segments.filter(segment => !!segment.colour).map(segment => segment.colour);
    const distinctColours = PlanUtil.distinctColours(colourValues);
    const colourGroups = distinctColours.map(colour => this.colour(colour));
    return colourGroups.join(" > ");
  }

  colour(colour: string): string {
    const splitted = List<string>(colour.split(";"));
    const translatedColours = splitted.map(colourKey => {
      const translation = this.translate(colourKey);
      if (translation.length === 0) {
        return colourKey;
      }
      return translation;
    });
    return translatedColours.join("-");
  }
}
