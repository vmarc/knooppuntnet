import { Injectable } from '@angular/core';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { filter } from 'rxjs/operators';
import { withLatestFrom } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { selectRouteParam } from '../../../core/core.state';
import { AppState } from '../../../core/core.state';
import { actionPreferencesItemsPerPage } from '../../../core/preferences/preferences.actions';
import { actionPreferencesImpact } from '../../../core/preferences/preferences.actions';
import { selectPreferencesItemsPerPage } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
import { actionRouteChangesFilterOption } from './route.actions';
import { actionRouteChangesPageIndex } from './route.actions';
import { actionRouteId } from './route.actions';
import { actionRouteChangesPageLoaded } from './route.actions';
import { actionRouteChangesPageInit } from './route.actions';
import { actionRouteMapPageInit } from './route.actions';
import { actionRouteDetailsPageInit } from './route.actions';
import { actionRouteDetailsPageLoaded } from './route.actions';
import { actionRouteMapPageLoaded } from './route.actions';
import { selectRouteChangesPage } from './route.selectors';
import { selectRouteChangesParameters } from './route.selectors';

@Injectable()
export class RouteEffects {
  routeDetails = createEffect(() =>
    this.actions$.pipe(
      ofType(actionRouteDetailsPageInit),
      withLatestFrom(this.store.select(selectRouteParam('routeId'))),
      mergeMap(([{}, routeId]) => {
        this.store.dispatch(actionRouteId({ routeId }));
        return this.appService
          .routeDetails(routeId)
          .pipe(map((response) => actionRouteDetailsPageLoaded({ response })));
      })
    )
  );

  routeMap = createEffect(() =>
    this.actions$.pipe(
      ofType(actionRouteMapPageInit),
      withLatestFrom(this.store.select(selectRouteParam('routeId'))),
      mergeMap(([{}, routeId]) => {
        this.store.dispatch(actionRouteId({ routeId }));
        return this.appService
          .routeMap(routeId)
          .pipe(map((response) => actionRouteMapPageLoaded({ response })));
      })
    )
  );

  routeChanges = createEffect(() =>
    this.actions$.pipe(
      ofType(actionRouteChangesPageInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId')),
        this.store.select(selectPreferencesImpact),
        this.store.select(selectPreferencesItemsPerPage),
        this.store.select(selectRouteChangesParameters)
      ),
      mergeMap(
        ([
          {},
          routeId,
          preferencesImpact,
          preferencesItemsPerPage,
          urlChangesParameters,
        ]) => {
          const itemsPerPage =
            urlChangesParameters?.itemsPerPage ?? preferencesItemsPerPage;
          const pageIndex = urlChangesParameters?.pageIndex ?? 0;
          const impact = urlChangesParameters?.impact ?? preferencesImpact;
          const changesParameters: ChangesParameters = {
            ...urlChangesParameters,
            itemsPerPage,
            pageIndex,
            impact,
          };
          this.store.dispatch(actionRouteId({ routeId }));
          return this.appService
            .routeChanges(routeId, changesParameters)
            .pipe(
              map((response) => actionRouteChangesPageLoaded({ response }))
            );
        }
      )
    )
  );

  routeChangesPageUpdate = createEffect(() =>
    this.actions$.pipe(
      ofType(
        actionRouteChangesPageIndex,
        actionPreferencesImpact,
        actionPreferencesItemsPerPage,
        actionRouteChangesFilterOption
      ),
      tap((action) =>
        console.log(`routeChangesPageUpdate ` + JSON.stringify(action, null, 2))
      ),
      withLatestFrom(this.store.select(selectRouteChangesPage)),
      // continue only if we are currently on the changes page!!
      filter(([{}, response]) => !!response),
      map(([{}, {}]) => {
        console.log(
          `routeChangesPageUpdate: dispatch actionRouteChangesPageInit`
        );
        return actionRouteChangesPageInit();
      })
    )
  );

  constructor(
    private actions$: Actions,
    private store: Store<AppState>,
    private appService: AppService
  ) {}
}
