import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Params } from '@angular/router';
import { NetworkType } from '@api/custom/network-type';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';
import { MapPosition } from '@app/components/ol/domain/map-position';
import { MapMode } from '@app/components/ol/services/map-mode';
import { selectRouteParam } from '@app/core/core.state';
import { actionPlannerLoad } from '@app/map/planner/store/planner-actions';
import { actionPlannerPageInit } from '@app/map/planner/store/planner-actions';
import { PlannerState } from '@app/map/planner/store/planner-state';
import { concatLatestFrom } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { selectQueryParam } from '@app/core/core.state';

@Injectable()
export class PlannerEffects {
  private readonly defaultPoiLayerStates: MapLayerState[] = [
    { layerName: 'hiking-biking', visible: true },
    { layerName: 'landmarks', visible: true },
    { layerName: 'restaurants', visible: true },
    { layerName: 'places-to-stay', visible: true },
    { layerName: 'tourism', visible: true },
    { layerName: 'amenity', visible: false },
    { layerName: 'shops', visible: false },
    { layerName: 'foodshops', visible: false },
    { layerName: 'sports', visible: false },
  ];

  // noinspection JSUnusedGlobalSymbols
  plannerPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionPlannerPageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('networkType')),
        this.store.select(selectQueryParam('position')),
        this.store.select(selectQueryParam('mode')),
        this.store.select(selectQueryParam('result')),
        this.store.select(selectQueryParam('layers')),
        this.store.select(selectQueryParam('pois')),
        this.store.select(selectQueryParam('poiLayers')),
      ]),
      map(
        ([
          _,
          networkTypeParam,
          positionParam,
          mode,
          result,
          layers,
          poisParam,
          poiLayers,
        ]) => {
          const networkType = !!networkTypeParam
            ? NetworkType[networkTypeParam]
            : NetworkType.hiking;
          const position = MapPosition.fromQueryParam(positionParam); // TODO default? or let map logic set initial view extent?

          let mapMode = MapMode.surface;
          if (mode === 'survey') {
            mapMode = MapMode.survey;
          } else if (mode === 'analysis') {
            mapMode = MapMode.analysis;
          }

          let resultMode = 'compact';
          if (result === 'details') {
            resultMode = 'details';
          }

          let defaultLayerStates: MapLayerState[] = [
            { layerName: 'osm-background', visible: false },
            { layerName: 'kpn-background', visible: false },
            { layerName: networkType, visible: true },
          ];

          if (networkType === NetworkType.hiking) {
            defaultLayerStates.push({
              layerName: 'netherlands-hiking',
              visible: false,
            });
            defaultLayerStates.push({
              layerName: 'flanders-hiking',
              visible: false,
            });
          }

          let layerStates = defaultLayerStates;
          if (!!layers) {
            layerStates = defaultLayerStates.map((defaultLayerState) => {
              const visible = poiLayers.includes(defaultLayerState.layerName);
              return {
                ...defaultLayerState,
                visible,
              };
            });
          }

          const pois = !!poisParam ? poisParam === 'true' : false;

          let poiLayerStates: MapLayerState[] = [];
          if (!!poiLayers) {
            poiLayerStates = this.defaultPoiLayerStates.map(
              (defaultLayerState) => {
                const visible = poiLayers.includes(defaultLayerState.layerName);
                return {
                  ...defaultLayerState,
                  visible,
                };
              }
            );
          } else {
            layerStates = this.defaultPoiLayerStates;
          }

          const state: PlannerState = {
            networkType,
            position,
            mapMode,
            resultMode,
            layerStates,
            pois,
            poiLayerStates,
          };

          return actionPlannerLoad(state);
        }
      )
    );
  });

  constructor(
    private actions$: Actions,
    private store: Store,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  private;

  navigate(state: PlannerState): Promise<boolean> {
    const position = state.position.toQueryParam();
    const mode = `mapMode=${state.mapMode}`;
    const result = state.resultMode;
    const layers = state.layerStates
      .filter((ls) => ls.visible)
      .map((ls) => ls.layerName)
      .join(',');
    const pois = state.pois.toString();
    const poiLayers = state.poiLayerStates
      .filter((ls) => ls.visible)
      .map((ls) => ls.layerName)
      .join(',');

    const queryParams: Params = {
      mode,
      position,
      result,
      layers,
      pois,
      poiLayers,
    };

    return this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
    });
  }
}
