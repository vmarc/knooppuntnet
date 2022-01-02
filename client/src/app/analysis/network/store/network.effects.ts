import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Params } from '@angular/router';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { concatLatestFrom } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { QueryParams } from '../../../base/query-params';
import { selectQueryParams } from '../../../core/core.state';
import { selectRouteParams } from '../../../core/core.state';
import { selectRouteParam } from '../../../core/core.state';
import { AppState } from '../../../core/core.state';
import { selectPreferencesPageSize } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
import { actionNetworkChangesLoad } from './network.actions';
import { actionNetworkChangesImpact } from './network.actions';
import { actionNetworkChangesPageSize } from './network.actions';
import { actionNetworkId } from './network.actions';
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
import { selectNetworkId } from './network.selectors';
import { selectNetworkChangesParameters } from './network.selectors';

@Injectable()
export class NetworkEffects {
  networkDetailsPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkDetailsPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('networkId'))),
      tap(([{}, networkId]) => {
        this.store.dispatch(actionNetworkId({ networkId: +networkId }));
      }),
      mergeMap(([{}, networkId]) =>
        this.appService
          .networkDetails(+networkId)
          .pipe(map((response) => actionNetworkDetailsPageLoaded({ response })))
      )
    )
  );

  networkNodesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkNodesPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('networkId'))),
      tap(([{}, networkId]) => {
        this.store.dispatch(actionNetworkId({ networkId: +networkId }));
      }),
      mergeMap(([{}, networkId]) =>
        this.appService
          .networkNodes(+networkId)
          .pipe(map((response) => actionNetworkNodesPageLoaded({ response })))
      )
    )
  );

  networkRoutesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkRoutesPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('networkId'))),
      tap(([{}, networkId]) => {
        this.store.dispatch(actionNetworkId({ networkId: +networkId }));
      }),
      mergeMap(([{}, networkId]) =>
        this.appService
          .networkRoutes(+networkId)
          .pipe(map((response) => actionNetworkRoutesPageLoaded({ response })))
      )
    )
  );

  networkFactsPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkFactsPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('networkId'))),
      tap(([{}, networkId]) => {
        this.store.dispatch(actionNetworkId({ networkId: +networkId }));
      }),
      mergeMap(([{}, networkId]) =>
        this.appService
          .networkFacts(+networkId)
          .pipe(map((response) => actionNetworkFactsPageLoaded({ response })))
      )
    )
  );

  networkMapPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkMapPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('networkId'))),
      tap(([{}, networkId]) => {
        this.store.dispatch(actionNetworkId({ networkId: +networkId }));
      }),
      mergeMap(([{}, networkId]) =>
        this.appService
          .networkMap(+networkId)
          .pipe(map((response) => actionNetworkMapPageLoaded({ response })))
      )
    )
  );

  networkChangesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkChangesPageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParams),
        this.store.select(selectQueryParams),
        this.store.select(selectPreferencesImpact),
        this.store.select(selectPreferencesPageSize),
      ]),
      map(
        ([
          {},
          routeParams,
          queryParams,
          preferencesImpact,
          preferencesPageSize,
        ]) => {
          const queryParamsUtil = new QueryParams(queryParams);
          const changesParameters = queryParamsUtil.changesParameters(
            preferencesImpact,
            preferencesPageSize
          );
          const networkId = +routeParams['networkId'];
          return actionNetworkChangesLoad({ networkId, changesParameters });
        }
      )
    )
  );

  networkChangesPageLoad = createEffect(() =>
    this.actions$.pipe(
      ofType(
        actionNetworkChangesLoad,
        actionNetworkChangesImpact,
        actionNetworkChangesPageSize,
        actionNetworkChangesPageIndex,
        actionNetworkChangesFilterOption
      ),
      concatLatestFrom(() => [
        this.store.select(selectNetworkId),
        this.store.select(selectNetworkChangesParameters),
      ]),
      tap(([{}, {}, changesParameters]) => {
        this.navigate(changesParameters);
      }),
      mergeMap(([{}, networkId, changesParameters]) => {
        return this.appService
          .networkChanges(+networkId, changesParameters)
          .pipe(
            map((response) => actionNetworkChangesPageLoaded({ response }))
          );
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
