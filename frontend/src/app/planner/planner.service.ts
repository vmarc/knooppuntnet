import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { PlanRoute } from '@api/common/planner';
import { PreferencesService } from '@app/core';
import { ApiService } from '@app/services';
import { List } from 'immutable';
import Map from 'ol/Map';
import { PlannerContext } from './domain/context/planner-context';
import { PlannerCursorImpl } from './domain/context/planner-cursor-impl';
import { PlannerElasticBandImpl } from './domain/context/planner-elastic-band-impl';
import { PlannerHighlightLayer } from './domain/context/planner-highlight-layer';
import { PlannerHighlighterImpl } from './domain/context/planner-highlighter-impl';
import { PlannerLegRepositoryImpl } from './domain/context/planner-leg-repository-impl';
import { PlannerMarkerLayerImpl } from './domain/context/planner-marker-layer-impl';
import { PlannerPopupService } from './domain/context/planner-popup-service';
import { PlannerRouteLayerImpl } from './domain/context/planner-route-layer-impl';
import { PlannerEngine } from './domain/interaction/planner-engine';
import { PlannerEngineImpl } from './domain/interaction/planner-engine-impl';
import { PlanUtil } from './domain/plan/plan-util';
import { PlannerTranslations } from './util/planner-translations';

@Injectable({
  providedIn: 'root',
})
export class PlannerService {
  private readonly apiService = inject(ApiService);
  private readonly preferencesService = inject(PreferencesService);
  private readonly plannerPopupService = inject(PlannerPopupService);

  engine: PlannerEngine;

  private readonly routeLayer = new PlannerRouteLayerImpl();
  private readonly markerLayer = new PlannerMarkerLayerImpl();
  private readonly cursor = new PlannerCursorImpl();
  private readonly elasticBand = new PlannerElasticBandImpl();
  private readonly highlightLayer = new PlannerHighlightLayer();
  private readonly highlighter = new PlannerHighlighterImpl(this.highlightLayer);
  private readonly legRepository = new PlannerLegRepositoryImpl(this.apiService);
  readonly context: PlannerContext = new PlannerContext(
    this.routeLayer,
    this.markerLayer,
    this.cursor,
    this.elasticBand,
    this.highlighter,
    this.legRepository,
    this.plannerPopupService,
    this.preferencesService.planProposed
  );

  constructor() {
    this.engine = new PlannerEngineImpl(this.context);
  }

  init(map: Map): void {
    this.cursor.addToMap(map);
    this.routeLayer.addToMap(map);
    this.markerLayer.addToMap(map);
    this.elasticBand.addToMap(map);
    this.highlightLayer.addToMap(map);
    this.plannerPopupService.addToMap(map);
  }

  hasColour(planRoute: PlanRoute): boolean {
    return planRoute.segments.filter((segment) => !!segment.colour).length > 0;
  }

  colours(planRoute: PlanRoute): string {
    const colourValues = planRoute.segments
      .filter((segment) => !!segment.colour)
      .map((segment) => segment.colour);
    const distinctColours = PlanUtil.distinctColours(List(colourValues));
    const colourGroups = distinctColours.map((colour) => PlannerTranslations.colour(colour));
    return colourGroups.join(' > ');
  }
}
