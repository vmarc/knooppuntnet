import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Params } from '@angular/router';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { from } from 'rxjs';
import { filter } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
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
import { actionNetworkChangesFilterOption } from './network.actions';
import { actionNetworkChangesPageIndex } from './network.actions';
import { actionNetworkNodesPageInit } from './network.actions';
import { actionNetworkRoutesPageInit } from './network.actions';
import { actionNetworkFactsPageInit } from './network.actions';
import { actionNetworkMapPageInit } from './network.actions';
import { actionNetworkChangesPageInit } from './network.actions';
import { actionNetworkDetailsPageInit } from './network.actions';
import { actionNetworkRoutesPageLoaded } from './network.actions';
import { actionNetworkFactsPageLoaded } from './network.actions';
import { actionNetworkMapPageLoaded } from './network.actions';
import { actionNetworkChangesPageLoaded } from './network.actions';
import { actionNetworkDetailsPageLoaded } from './network.actions';
import { actionNetworkNodesPageLoaded } from './network.actions';
import { selectNetworkChangesPage } from './network.selectors';
import { selectNetworkChangesParameters } from './network.selectors';

@Injectable()
export class NetworkEffects {
  networkDetailsPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkDetailsPageInit),
      withLatestFrom(this.store.select(selectRouteParam('networkId'))),
      mergeMap(([action, networkId]) =>
        this.appService
          .networkDetails(+networkId)
          .pipe(map((response) => actionNetworkDetailsPageLoaded({ response })))
      )
    )
  );

  networkNodesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkNodesPageInit),
      withLatestFrom(this.store.select(selectRouteParam('networkId'))),
      mergeMap(([action, networkId]) =>
        this.appService
          .networkNodes(+networkId)
          .pipe(map((response) => actionNetworkNodesPageLoaded({ response })))
      )
    )
  );

  networkRoutesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkRoutesPageInit),
      withLatestFrom(this.store.select(selectRouteParam('networkId'))),
      mergeMap(([action, networkId]) =>
        this.appService
          .networkRoutes(+networkId)
          .pipe(map((response) => actionNetworkRoutesPageLoaded({ response })))
      )
    )
  );

  networkFactsPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkFactsPageInit),
      withLatestFrom(this.store.select(selectRouteParam('networkId'))),
      mergeMap(([action, networkId]) =>
        this.appService
          .networkFacts(+networkId)
          .pipe(map((response) => actionNetworkFactsPageLoaded({ response })))
      )
    )
  );

  networkMapPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkMapPageInit),
      withLatestFrom(this.store.select(selectRouteParam('networkId'))),
      mergeMap(([action, networkId]) =>
        this.appService
          .networkMap(+networkId)
          .pipe(map((response) => actionNetworkMapPageLoaded({ response })))
      )
    )
  );

  networkChangesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkChangesPageInit),
      withLatestFrom(
        this.store.select(selectRouteParam('networkId')),
        this.store.select(selectPreferencesImpact),
        this.store.select(selectPreferencesItemsPerPage),
        this.store.select(selectNetworkChangesParameters)
      ),
      map(
        ([
          action,
          networkId,
          preferencesImpact,
          preferencesItemsPerPage,
          urlChangesParameters,
        ]) => {
          const itemsPerPage =
            urlChangesParameters.itemsPerPage ?? preferencesItemsPerPage;
          const pageIndex = urlChangesParameters.pageIndex ?? 0;
          const impact = urlChangesParameters.impact ?? preferencesImpact;
          const changesParameters: ChangesParameters = {
            ...urlChangesParameters,
            itemsPerPage,
            pageIndex,
            impact,
          };
          return { networkId, changesParameters };
        }
      ),
      tap(({ networkId, changesParameters }) =>
        this.navigate(changesParameters)
      ),
      mergeMap(({ networkId, changesParameters }) => {
        return this.appService
          .networkChanges(+networkId, changesParameters)
          .pipe(
            map((response) => actionNetworkChangesPageLoaded({ response }))
          );
      })
    )
  );

  networkChangesPageUpdate = createEffect(() =>
    this.actions$.pipe(
      ofType(
        actionNetworkChangesPageIndex,
        actionPreferencesImpact,
        actionPreferencesItemsPerPage,
        actionNetworkChangesFilterOption
      ),
      withLatestFrom(this.store.select(selectNetworkChangesPage)),
      // continue only if we are currently on the changes page!!
      filter(([action, response]) => !!response),
      withLatestFrom(this.store.select(selectNetworkChangesParameters)),
      mergeMap(([[action, response], changesParameters]) => {
        const promise = this.navigate(changesParameters);
        return from(promise).pipe(map(actionNetworkChangesPageInit));
      })
    )
  );

  constructor(
    private actions$: Actions,
    private store: Store<AppState>,
    private router: Router,
    private route: ActivatedRoute,
    private appService: AppService
  ) {}

  private navigate(changesParameters: ChangesParameters): Promise<boolean> {
    const queryParams: Params = {
      ...changesParameters,
    };
    return this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
    });
  }
}
