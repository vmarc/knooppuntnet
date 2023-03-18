import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Params } from '@angular/router';
import { NetworkType } from '@api/custom/network-type';
import { MapLayerState } from '@app/components/ol/domain/map-layer-state';
import { MapPosition } from '@app/components/ol/domain/map-position';
import { MapMode } from '@app/components/ol/services/map-mode';
import { selectRouteParam } from '@app/core/core.state';
import { selectQueryParam } from '@app/core/core.state';
import { PlannerLayerService } from '@app/planner/services/planner-layer.service';
import { selectPlannerState } from '@app/planner/store/planner-selectors';
import { BrowserStorageService } from '@app/services/browser-storage.service';
import { concatLatestFrom } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { Coordinate } from 'ol/coordinate';
import { fromLonLat } from 'ol/proj';
import { from } from 'rxjs';
import { tap } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { actionPlannerMapMode } from './planner-actions';
import { actionPlannerNetworkType } from './planner-actions';
import { actionPlannerPoiGroupVisible } from './planner-actions';
import { actionPlannerPoisEnabled } from './planner-actions';
import { actionPlannerLayerVisible } from './planner-actions';
import { actionPlannerPosition } from './planner-actions';
import { actionPlannerLoad } from './planner-actions';
import { actionPlannerPageInit } from './planner-actions';
import { PlannerState } from './planner-state';

@Injectable()
export class PlannerEffects {
  private mapPositionKey = 'map-position';

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

          let position = MapPosition.fromQueryParam(positionParam);
          if (!position) {
            const mapPositionString = this.storage.get(this.mapPositionKey);
            if (!!mapPositionString) {
              position = MapPosition.fromQueryParam(mapPositionString);
              console.log(
                `initial position from lcoal storage: ${position.toQueryParam()}`
              );
            } else {
              // TODO replace temporary code
              const a: Coordinate = fromLonLat([2.24, 50.16]);
              const b: Coordinate = fromLonLat([10.56, 54.09]);
              // const extent: Extent = [a[0], a[1], b[0], b[1]];
              const x = (b[0] - a[0]) / 2 + a[0];
              const y = (b[1] - a[1]) / 2 + a[1];
              position = new MapPosition(14, x, y, 0);
              console.log(
                `initial position from temporary code: ${position.toQueryParam()}`
              );
            }
          } else {
            console.log(
              `initial position from query params: ${position.toQueryParam()}`
            );
          }

          let mapMode: MapMode = 'surface';
          if (mode === 'survey') {
            mapMode = 'survey';
          } else if (mode === 'analysis') {
            mapMode = 'analysis';
          }

          let resultMode = 'compact';
          if (result === 'details') {
            resultMode = 'details';
          }

          const layerStates: MapLayerState[] = [];

          const pois: boolean = !!poisParam ? poisParam === 'true' : false;

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
            poiLayerStates = this.defaultPoiLayerStates;
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

  // noinspection JSUnusedGlobalSymbols
  plannerPosition = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionPlannerPosition),
        concatLatestFrom(() => this.store.select(selectPlannerState)),
        mergeMap(([mapPosition, state]) => {
          this.storage.set(this.mapPositionKey, mapPosition.toQueryParam());
          const promise = this.navigate(state);
          return from(promise);
        })
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  layersVisibility = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(
          // actionPlannerLoad,
          actionPlannerLayerVisible,
          actionPlannerNetworkType,
          actionPlannerMapMode,
          actionPlannerPosition
        ),
        concatLatestFrom(() => this.store.select(selectPlannerState)),
        tap(([_, state]) => {
          console.log('layersVisibility effect');
          this.updateLayerVisibility(state);
        })
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  plannerNavigate = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(
          actionPlannerLayerVisible,
          actionPlannerPoisEnabled,
          actionPlannerPoiGroupVisible,
          actionPlannerNetworkType,
          actionPlannerPosition
        ),
        concatLatestFrom(() => this.store.select(selectPlannerState)),
        mergeMap(([_, state]) => {
          console.log('navigate effect');
          const promise = this.navigate(state);
          return from(promise);
        })
      );
    },
    { dispatch: false }
  );

  constructor(
    private actions$: Actions,
    private store: Store,
    private router: Router,
    private route: ActivatedRoute,
    private storage: BrowserStorageService,
    private plannerLayerService: PlannerLayerService
  ) {}

  private navigate(state: PlannerState): Promise<boolean> {
    const position = state.position.toQueryParam();
    const mode = state.mapMode;
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

    return this.router.navigate(['map', state.networkType], {
      queryParams,
    });
  }

  private updateLayerVisibility(state: PlannerState): void {
    this.plannerLayerService.updateLayerVisibility(
      state.layerStates,
      state.networkType,
      state.mapMode,
      state.position.zoom
    );
  }
}
