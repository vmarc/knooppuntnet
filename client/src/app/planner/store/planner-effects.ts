import { Injectable } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { selectQueryParams, selectRouteParams } from '@app/core/core.state';
import { PlannerStateService } from '@app/planner/services/planner-state.service';
import { selectPlannerState } from '@app/planner/store/planner-selectors';
import { BrowserStorageService } from '@app/services/browser-storage.service';
import { PoiService } from '@app/services/poi.service';
import { Actions, concatLatestFrom, createEffect, ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { from, Observable } from 'rxjs';
import { map, mergeMap, tap } from 'rxjs/operators';
import { actionPlannerInit } from './planner-actions';
import { actionPlannerLayerVisible } from './planner-actions';
import { actionPlannerLoad } from './planner-actions';
import { actionPlannerMapViewInit } from './planner-actions';
import { actionPlannerMapMode } from './planner-actions';
import { actionPlannerNetworkType } from './planner-actions';
import { actionPlannerPoiGroupVisible } from './planner-actions';
import { actionPlannerPoisEnabled } from './planner-actions';
import { actionPlannerPosition } from './planner-actions';
import { PlannerState } from './planner-state';
import { PlannerMapService } from '@app/planner/pages/planner/planner-map.service';

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
      mergeMap(([_, routeParams, queryParams]) => {
        const state = this.plannerStateService.toPlannerState(
          routeParams,
          queryParams
        );
        return this.navigate(state).pipe(map(() => actionPlannerLoad(state)));
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  plannerPosition = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionPlannerPosition),
        concatLatestFrom(() => this.store.select(selectPlannerState)),
        mergeMap(([{ mapPosition }, state]) => {
          this.storage.set(
            this.plannerStateService.plannerPositionKey,
            mapPosition.toQueryParam()
          );
          return this.navigate(state);
        })
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  plannerMapViewInit = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionPlannerMapViewInit),
        concatLatestFrom(() => this.store.select(selectPlannerState)),
        tap(([_, plannerState]) => this.plannerMapService.init(plannerState))
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  networkChange = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionPlannerNetworkType),
        tap(({ networkType }) =>
          this.plannerMapService.handleNetworkChange(networkType)
        )
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  layersVisibility = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(
          actionPlannerMapViewInit,
          actionPlannerLayerVisible,
          actionPlannerNetworkType,
          actionPlannerMapMode,
          actionPlannerPosition,
          actionPlannerPoisEnabled
        ),
        concatLatestFrom(() => this.store.select(selectPlannerState)),
        tap(([_, state]) => this.updateLayerVisibility(state))
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
        mergeMap(([_, state]) => this.navigate(state))
      );
    },
    { dispatch: false }
  );

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

  private updateLayerVisibility(state: PlannerState): void {
    this.plannerMapService.plannerUpdateLayerVisibility(
      state.networkType,
      state.mapMode,
      state.pois
    );
  }
}
