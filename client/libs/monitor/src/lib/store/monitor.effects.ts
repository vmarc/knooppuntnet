import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { MonitorChangesParameters } from '@api/common/monitor';
import { selectRouteParam } from '@app/core';
import { selectPreferencesPageSize } from '@app/core';
import { selectPreferencesImpact } from '@app/core';
import { concatLatestFrom } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { MonitorService } from '../monitor.service';
import { actionMonitorRouteChangesPageIndex } from './monitor.actions';
import { actionMonitorRouteChangesPageInit } from './monitor.actions';
import { actionMonitorRouteChangePageInit } from './monitor.actions';
import { actionMonitorRouteChangePageLoaded } from './monitor.actions';
import { actionMonitorRouteChangesPageLoaded } from './monitor.actions';

@Injectable()
export class MonitorEffects {
  // noinspection JSUnusedGlobalSymbols
  monitorRouteChangesPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(
        actionMonitorRouteChangesPageInit,
        actionMonitorRouteChangesPageIndex
      ),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('monitorRouteId')),
        this.store.select(selectPreferencesPageSize),
        this.store.select(selectPreferencesImpact),
      ]),
      mergeMap(([_, monitorRouteId, pageSize, pageIndex, impact]) => {
        const parameters: MonitorChangesParameters = {
          pageSize,
          pageIndex,
          impact,
        };
        return this.monitorService
          .routeChanges(monitorRouteId, parameters)
          .pipe(
            map((response) => actionMonitorRouteChangesPageLoaded(response))
          );
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteChangePageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteChangePageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('monitorRouteId')),
        this.store.select(selectRouteParam('changeSetId')),
        this.store.select(selectRouteParam('replicationNumber')),
      ]),
      mergeMap(([_, monitorRouteId, changeSetId, replicationNumber]) =>
        this.monitorService
          .routeChange(monitorRouteId, changeSetId, replicationNumber)
          .pipe(map((response) => actionMonitorRouteChangePageLoaded(response)))
      )
    );
  });

  constructor(
    private actions$: Actions,
    private store: Store,
    private router: Router,
    private monitorService: MonitorService
  ) {}
}
