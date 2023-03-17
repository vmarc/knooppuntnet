import { Injectable } from '@angular/core';
import { PlanRoute } from '@api/common/planner/plan-route';
import { Store } from '@ngrx/store';
import { List } from 'immutable';
import { Map as TranslationMap } from 'immutable';
import Map from 'ol/Map';
import { AppService } from '../app.service';
import { MapService } from '../components/ol/services/map.service';
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
  private readonly translations: TranslationMap<string, string> =
    this.buildTranslations();
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
    private store: Store
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

  translate(key: string): string {
    return this.translations.get(key);
  }

  hasColour(planRoute: PlanRoute): boolean {
    return planRoute.segments.filter((segment) => !!segment.colour).length > 0;
  }

  colours(planRoute: PlanRoute): string {
    const colourValues = planRoute.segments
      .filter((segment) => !!segment.colour)
      .map((segment) => segment.colour);
    const distinctColours = PlanUtil.distinctColours(List(colourValues));
    const colourGroups = distinctColours.map((colour) => this.colour(colour));
    return colourGroups.join(' > ');
  }

  colour(colour: string): string {
    return new ColourTranslator(this.translations).translate(colour);
  }

  private buildTranslations(): TranslationMap<string, string> {
    const keysAndValues: Array<[string, string]> = [];

    keysAndValues.push(['head', $localize`:@@plan.head:Head`]);

    keysAndValues.push([
      'follow-colour',
      $localize`:@@plan.follow-colour:Follow colour`,
    ]);

    keysAndValues.push([
      'heading-north',
      $localize`:@@plan.heading.north:north`,
    ]);
    keysAndValues.push([
      'heading-north-east',
      $localize`:@@plan.heading.north-east:north-east`,
    ]);
    keysAndValues.push(['heading-east', $localize`:@@plan.heading.east:east`]);
    keysAndValues.push([
      'heading-south-east',
      $localize`:@@plan.heading.south-east:south-east`,
    ]);
    keysAndValues.push([
      'heading-south',
      $localize`:@@plan.heading.south:south`,
    ]);
    keysAndValues.push([
      'heading-south-west',
      $localize`:@@plan.heading.south-west:south-west`,
    ]);
    keysAndValues.push(['heading-west', $localize`:@@plan.heading.west:west`]);
    keysAndValues.push([
      'heading-north-west',
      $localize`:@@plan.heading.north-west:north-west`,
    ]);

    keysAndValues.push([
      'command-continue',
      $localize`:@@plan.command.continue:Continue`,
    ]);
    keysAndValues.push([
      'command-turn-slight-left',
      $localize`:@@plan.command.turn-slight-left:Slight left`,
    ]);
    keysAndValues.push([
      'command-turn-slight-right',
      $localize`:@@plan.command.turn-slight-right:Slight right`,
    ]);
    keysAndValues.push([
      'command-turn-left',
      $localize`:@@plan.command.turn-left:Turn left`,
    ]);
    keysAndValues.push([
      'command-turn-right',
      $localize`:@@plan.command.turn-right:Turn right`,
    ]);
    keysAndValues.push([
      'command-turn-sharp-left',
      $localize`:@@plan.command.turn-sharp-left:Sharp left`,
    ]);
    keysAndValues.push([
      'command-turn-sharp-right',
      $localize`:@@plan.command.turn-sharp-right:Sharp right`,
    ]);

    keysAndValues.push(['black', $localize`:@@route.colour.black:black`]);
    keysAndValues.push(['blue', $localize`:@@route.colour.blue:blue`]);
    keysAndValues.push(['brown', $localize`:@@route.colour.brown:brown`]);
    keysAndValues.push(['gray', $localize`:@@route.colour.gray:gray`]);
    keysAndValues.push(['green', $localize`:@@route.colour.green:green`]);
    keysAndValues.push(['grey', $localize`:@@route.colour.grey:grey`]);
    keysAndValues.push(['orange', $localize`:@@route.colour.orange:orange`]);
    keysAndValues.push(['purple', $localize`:@@route.colour.purple:purple`]);
    keysAndValues.push(['red', $localize`:@@route.colour.red:red`]);
    keysAndValues.push(['violet', $localize`:@@route.colour.violet:violet`]);
    keysAndValues.push(['white', $localize`:@@route.colour.white:white`]);
    keysAndValues.push(['yellow', $localize`:@@route.colour.yellow:yellow`]);

    keysAndValues.push(['or', $localize`:@@route.colour.or:or`]);

    return TranslationMap<string, string>(keysAndValues);
  }
}
