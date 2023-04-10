import { Injectable } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { selectQueryParams, selectRouteParams } from '@app/core/core.state';
import { PlannerMapService } from '@app/planner/pages/planner/planner-map.service';
import { PlannerStateService } from '@app/planner/services/planner-state.service';
import { selectPlannerState } from '@app/planner/store/planner-selectors';
import { selectPlannerMapMode } from '@app/planner/store/planner-selectors';
import { selectPlannerPois } from '@app/planner/store/planner-selectors';
import { BrowserStorageService } from '@app/services/browser-storage.service';
import { PoiService } from '@app/services/poi.service';
import { Actions, concatLatestFrom, createEffect, ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { from, Observable } from 'rxjs';
import { mergeMap, tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { actionPlannerMapViewInit } from './planner-actions';
import { actionPlannerNetworkType } from './planner-actions';
import { actionPlannerPoiGroupVisible } from './planner-actions';
import { actionPlannerPoisEnabled } from './planner-actions';
import { actionPlannerPosition } from './planner-actions';
import { actionPlannerLayerStates } from './planner-actions';
import { actionPlannerInit } from './planner-actions';
import { actionPlannerLoad } from './planner-actions';
import { actionPlannerMapFinalized } from './planner-actions';
import { PlannerState } from './planner-state';
import { initialState } from './planner-state';

@Injectable()
export class PlannerEffects {
  // noinspection JSUnusedGlobalSymbols
  plannerPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionPlannerInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParams),
        this.store.select(selectQueryParams),
      ]),
      map(([_, routeParams, queryParams]) => {
        const networkType =
          this.plannerStateService.parseNetworkType(routeParams);
        const mapMode = this.plannerStateService.parseMapMode(queryParams);
        const resultMode =
          this.plannerStateService.parseResultMode(queryParams);
        const state = { ...initialState, networkType, mapMode, resultMode };
        return actionPlannerLoad({ networkType, mapMode, resultMode });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  plannerMapViewInit = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionPlannerMapViewInit),
        concatLatestFrom(() => [
          this.store.select(selectPlannerState),
          this.store.select(selectRouteParams),
          this.store.select(selectQueryParams),
        ]),
        tap(([_, state, routeParams, queryParams]) => {
          this.plannerMapService.init(
            state.networkType,
            state.mapMode,
            state.resultMode,
            queryParams
          );
        })
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  networkChange = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionPlannerNetworkType),
        concatLatestFrom(() => [
          this.store.select(selectPlannerMapMode),
          this.store.select(selectPlannerPois),
        ]),
        tap(([{ networkType }, mapMode, pois]) =>
          this.plannerMapService.handleNetworkChange(networkType, mapMode, pois)
        )
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  plannerNavigate = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(
          actionPlannerMapFinalized,
          actionPlannerPoisEnabled,
          actionPlannerPoiGroupVisible,
          actionPlannerNetworkType,
          actionPlannerPosition,
          actionPlannerLayerStates
        ),
        concatLatestFrom(() => [this.store.select(selectPlannerState)]),
        mergeMap(([action, state]) => {
          console.log('navigate action=' + action.type);
          return this.navigate(state);
        })
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  plannerPoisEnabled = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionPlannerPoisEnabled),
        tap(({ enabled }) => this.poiService.updateEnabled(enabled))
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  plannerPoiGroupVisible = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionPlannerPoiGroupVisible),
        tap(({ groupName, visible }) =>
          this.poiService.updateGroupEnabled(groupName, visible)
        )
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
    private plannerStateService: PlannerStateService,
    private poiService: PoiService,
    private plannerMapService: PlannerMapService
  ) {}

  private navigate(state: PlannerState): Observable<boolean> {
    const queryParams = this.plannerStateService.toQueryParams(state);
    const promise = this.router.navigate(['map', state.networkType], {
      queryParams,
    });
    return from(promise);
  }
}