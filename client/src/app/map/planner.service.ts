import { Injectable } from '@angular/core';
import { PlanRoute } from '@api/common/planner/plan-route';
import { Store } from '@ngrx/store';
import { Map as TranslationMap } from 'immutable';
import Map from 'ol/Map';
import { BehaviorSubject } from 'rxjs';
import { AppService } from '../app.service';
import { MapService } from '../components/ol/services/map.service';
import { AppState } from '../core/core.state';
import { selectPreferencesPlanProposed } from '../core/preferences/preferences.selectors';
import { PlannerContext } from './planner/context/planner-context';
import { PlannerCursorImpl } from './planner/context/planner-cursor-impl';
import { PlannerElasticBandImpl } from './planner/context/planner-elastic-band-impl';
import { PlannerHighlightLayer } from './planner/context/planner-highlight-layer';
import { PlannerHighlighterImpl } from './planner/context/planner-highlighter-impl';
import { PlannerLegRepositoryImpl } from './planner/context/planner-leg-repository-impl';
import { PlannerMarkerLayerImpl } from './planner/context/planner-marker-layer-impl';
import { PlannerOverlayImpl } from './planner/context/planner-overlay-impl';
import { PlannerRouteLayerImpl } from './planner/context/planner-route-layer-impl';
import { PlannerEngine } from './planner/interaction/planner-engine';
import { PlannerEngineImpl } from './planner/interaction/planner-engine-impl';
import { PlanUtil } from './planner/plan/plan-util';
import { ColourTranslator } from './planner/services/colour-translator';

@Injectable({
  providedIn: 'root',
})
export class PlannerService {
  engine: PlannerEngine;
  resultMode$ = new BehaviorSubject<string>('compact');
  private translations: TranslationMap<string, string> = null;
  private readonly routeLayer = new PlannerRouteLayerImpl();
  private readonly markerLayer = new PlannerMarkerLayerImpl();
  private readonly cursor = new PlannerCursorImpl();
  private readonly elasticBand = new PlannerElasticBandImpl();
  private readonly highlightLayer = new PlannerHighlightLayer();
  private readonly highlighter = new PlannerHighlighterImpl(
    this.highlightLayer
  );
  private readonly legRepository = new PlannerLegRepositoryImpl(
    this.appService
  );
  private readonly overlay = new PlannerOverlayImpl(this.mapService);
  context: PlannerContext = new PlannerContext(
    this.routeLayer,
    this.markerLayer,
    this.cursor,
    this.elasticBand,
    this.highlighter,
    this.legRepository,
    this.overlay,
    this.store.select(selectPreferencesPlanProposed)
  );

  constructor(
    private appService: AppService,
    private mapService: MapService,
    private store: Store<AppState>
  ) {
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
        const id = span.getAttribute('id');
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
      return '';
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
    return colourGroups.join(' > ');
  }

  colour(colour: string): string {
    return new ColourTranslator(this.translations).translate(colour);
  }
}
