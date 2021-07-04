import { Injectable } from '@angular/core';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { withLatestFrom } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { selectRouteParam } from '../../../core/core.state';
import { AppState } from '../../../core/core.state';
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

@Injectable()
export class NetworkEffects {
  constructor(
    private actions$: Actions,
    private store: Store<AppState>,
    private appService: AppService
  ) {}

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
      withLatestFrom(this.store.select(selectRouteParam('networkId'))),
      mergeMap(([action, networkId]) => {
        const parameters: ChangesParameters = null;
        return this.appService
          .networkChanges(+networkId, parameters)
          .pipe(
            map((response) => actionNetworkChangesPageLoaded({ response }))
          );
      })
    )
  );
}
