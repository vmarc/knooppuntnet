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
import { from } from 'rxjs';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { PageParams } from '../../../base/page-params';
import { MapPosition } from '../../../components/ol/domain/map-position';
import { NetworkMapPosition } from '../../../components/ol/domain/network-map-position';
import { selectQueryParam } from '../../../core/core.state';
import { selectQueryParams } from '../../../core/core.state';
import { selectRouteParams } from '../../../core/core.state';
import { selectRouteParam } from '../../../core/core.state';
import { AppState } from '../../../core/core.state';
import { selectPreferencesPageSize } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
import { actionNetworkMapPageLoad } from './network.actions';
import { actionNetworkRoutesPageLoad } from './network.actions';
import { actionNetworkNodesPageLoad } from './network.actions';
import { actionNetworkFactsPageLoad } from './network.actions';
import { actionNetworkDetailsPageLoad } from './network.actions';
import { actionNetworkChangesLoad } from './network.actions';
import { actionNetworkChangesImpact } from './network.actions';
import { actionNetworkChangesPageSize } from './network.actions';
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
  // noinspection JSUnusedGlobalSymbols
  networkDetailsPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkDetailsPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('networkId'))),
      map(([{}, networkId]) =>
        actionNetworkDetailsPageLoad({ networkId: +networkId })
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  networkDetailsPageLoad = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkDetailsPageLoad),
      mergeMap((action) => this.appService.networkDetails(action.networkId)),
      map((response) => actionNetworkDetailsPageLoaded(response))
    )
  );

  // noinspection JSUnusedGlobalSymbols
  networkFactsPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkFactsPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('networkId'))),
      map(([{}, networkId]) =>
        actionNetworkFactsPageLoad({ networkId: +networkId })
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  networkFactsPageLoad = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkFactsPageLoad),
      mergeMap((action) => this.appService.networkFacts(action.networkId)),
      map((response) => actionNetworkFactsPageLoaded(response))
    )
  );

  // noinspection JSUnusedGlobalSymbols
  networkNodesPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkNodesPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('networkId'))),
      map(([{}, networkId]) =>
        actionNetworkNodesPageLoad({ networkId: +networkId })
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  networkNodesPageLoad = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkNodesPageLoad),
      mergeMap((action) => this.appService.networkNodes(action.networkId)),
      map((response) => actionNetworkNodesPageLoaded(response))
    )
  );

  // noinspection JSUnusedGlobalSymbols
  networkRoutesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkRoutesPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('networkId'))),
      map(([{}, networkId]) =>
        actionNetworkRoutesPageLoad({ networkId: +networkId })
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  networkRoutesPageLoad = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkRoutesPageLoad),
      mergeMap((action) => this.appService.networkRoutes(action.networkId)),
      map((response) => actionNetworkRoutesPageLoaded(response))
    )
  );

  // noinspection JSUnusedGlobalSymbols
  networkMapPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkMapPageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('networkId')),
        this.store.select(selectQueryParam('position')),
      ]),
      map(([{}, networkId, mapPositionString]) => {
        let mapPositionFromUrl: NetworkMapPosition = null;
        if (mapPositionString) {
          const mapPosition = MapPosition.fromQueryParam(mapPositionString);
          if (mapPosition) {
            mapPositionFromUrl = mapPosition.toNetworkMapPosition(
              mapPosition,
              +networkId
            );
          }
        }
        return actionNetworkMapPageLoad({
          networkId: +networkId,
          mapPositionFromUrl,
        });
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  networkMapPageLoad = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNetworkMapPageLoad),
      mergeMap((action) => {
        return this.appService.networkMap(action.networkId).pipe(
          map((response) =>
            actionNetworkMapPageLoaded({
              response,
              mapPositionFromUrl: action.mapPositionFromUrl,
            })
          )
        );
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
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
          const pageParams = new PageParams(routeParams, queryParams);
          const networkId = pageParams.networkId();
          const changesParameters = pageParams.changesParameters(
            preferencesImpact,
            preferencesPageSize
          );
          return actionNetworkChangesLoad({ networkId, changesParameters });
        }
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
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
      mergeMap(([{}, networkId, changesParameters]) => {
        const promise = this.navigate(changesParameters);
        return from(promise).pipe(
          mergeMap(() =>
            this.appService.networkChanges(+networkId, changesParameters)
          ),
          map((response) => actionNetworkChangesPageLoaded(response))
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
