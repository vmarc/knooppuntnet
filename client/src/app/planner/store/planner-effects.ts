import { Injectable } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { selectRouteParams } from '@app/core/core.state';
import { PlannerLayerService } from '@app/planner/services/planner-layer.service';
import { PlannerStateService } from '@app/planner/services/planner-state.service';
import { selectPlannerState } from '@app/planner/store/planner-selectors';
import { BrowserStorageService } from '@app/services/browser-storage.service';
import { PoiService } from '@app/services/poi.service';
import { Actions, concatLatestFrom, createEffect, ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { from, Observable } from 'rxjs';
import { map, mergeMap, tap } from 'rxjs/operators';
import {
  actionPlannerInit,
  actionPlannerLayerVisible,
  actionPlannerLoad,
  actionPlannerLoaded,
  actionPlannerMapMode,
  actionPlannerNetworkType,
  actionPlannerPoiGroupVisible,
  actionPlannerPoisEnabled,
  actionPlannerPosition,
} from './planner-actions';
import { PlannerState } from './planner-state';

@Injectable()
export class PlannerEffects {
  // noinspection JSUnusedGlobalSymbols
  plannerPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionPlannerInit),
      concatLatestFrom(() => this.store.select(selectRouteParams)),
      mergeMap(([_, queryParams]) => {
        const state = this.plannerStateService.toPlannerState(queryParams);
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
            this.plannerStateService.mapPositionKey,
            mapPosition.toQueryParam()
          );
          return this.navigate(state);
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
          actionPlannerLoaded,
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
    private plannerLayerService: PlannerLayerService,
    private plannerStateService: PlannerStateService,
    private poiService: PoiService
  ) {}

  private navigate(state: PlannerState): Observable<boolean> {
    const queryParams = this.plannerStateService.toQueryParams(state);
    const promise = this.router.navigate(['map', state.networkType], {
      queryParams,
    });
    return from(promise);
  }

  private updateLayerVisibility(state: PlannerState): void {
    this.plannerLayerService.updateLayerVisibility(
      state.layerStates,
      state.networkType,
      state.mapMode,
      state.position.zoom,
      state.pois
    );
  }
}
