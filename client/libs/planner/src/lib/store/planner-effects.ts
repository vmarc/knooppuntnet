import { Injectable } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { selectQueryParams, selectRouteParams } from '@app/core';
import { actionSharedSurveyDateInfoInit } from '@app/core';
import { BrowserStorageService } from '@app/services';
import { PoiService } from '@app/services';
import { Actions } from '@ngrx/effects';
import { concatLatestFrom } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { from } from 'rxjs';
import { Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { PlannerMapService } from '../pages/planner/planner-map.service';
import { actionPlannerMapMode } from './planner-actions';
import { actionPlannerMapViewInit } from './planner-actions';
import { actionPlannerNetworkType } from './planner-actions';
import { actionPlannerPoiGroupVisible } from './planner-actions';
import { actionPlannerPoisVisible } from './planner-actions';
import { actionPlannerPosition } from './planner-actions';
import { actionPlannerLayerStates } from './planner-actions';
import { actionPlannerInit } from './planner-actions';
import { actionPlannerLoad } from './planner-actions';
import { actionPlannerMapFinalized } from './planner-actions';
import { selectPlannerLayerStates } from './planner-selectors';
import { selectPlannerState } from './planner-selectors';
import { selectPlannerMapMode } from './planner-selectors';
import { PlannerState } from './planner-state';

@Injectable()
export class PlannerEffects {
  // noinspection JSUnusedGlobalSymbols
  plannerPageInitSurveyDayInfo = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionPlannerInit),
      map(() => actionSharedSurveyDateInfoInit())
    );
  });

  // noinspection JSUnusedGlobalSymbols
  plannerPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionPlannerInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParams),
        this.store.select(selectQueryParams),
      ]),
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      map(([_, routeParams, queryParams]) => {
        const state = this.plannerMapService.toPlannerState(
          routeParams,
          queryParams
        );
        return actionPlannerLoad({ state });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  plannerMapViewInit = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionPlannerMapViewInit),
        concatLatestFrom(() => [this.store.select(selectPlannerState)]),
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        tap(([_, state]) => {
          this.plannerMapService.init(state);
          this.navigate(state);
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
        concatLatestFrom(() => [this.store.select(selectPlannerMapMode)]),
        tap(([{ networkType }, mapMode]) =>
          this.plannerMapService.handleNetworkChange(networkType, mapMode)
        )
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  mapModeChange = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionPlannerMapMode),
        concatLatestFrom(() => [this.store.select(selectPlannerState)]),
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        mergeMap(([_, state]) => {
          this.plannerMapService.updateLayerVisibility();
          return this.navigate(state);
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
          actionPlannerMapFinalized,
          actionPlannerPoisVisible,
          actionPlannerPoiGroupVisible,
          actionPlannerNetworkType,
          actionPlannerPosition,
          actionPlannerLayerStates
        ),
        concatLatestFrom(() => [this.store.select(selectPlannerState)]),
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        mergeMap(([_, state]) => this.navigate(state))
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  plannerPoisEnabled = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionPlannerPoisVisible),
        concatLatestFrom(() => [this.store.select(selectPlannerLayerStates)]),
        tap(([{ visible }, layerStates]) => {
          this.poiService.updateEnabled(visible);

          this.plannerMapService.plannerUpdatePoiLayerVisibility(layerStates);
        })
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  plannerPoiGroupVisible = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionPlannerPoiGroupVisible),
        tap(({ groupName, visible }) => {
          this.poiService.updateGroupEnabled(groupName, visible);
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
    private poiService: PoiService,
    private plannerMapService: PlannerMapService
  ) {}

  private navigate(state: PlannerState): Observable<boolean> {
    const queryParams = this.plannerMapService.toQueryParams(state);
    const promise = this.router.navigate(['map', state.networkType], {
      queryParams,
    });
    return from(promise);
  }
}
