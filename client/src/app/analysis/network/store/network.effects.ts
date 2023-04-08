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
import { tap } from 'rxjs/operators';
import { AppService } from '@app/app.service';
import { PageParams } from '@app/base/page-params';
import { MapPosition } from '@app/components/ol/domain/map-position';
import { NetworkMapPosition } from '@app/components/ol/domain/network-map-position';
import { selectQueryParam } from '@app/core/core.state';
import { selectQueryParams } from '@app/core/core.state';
import { selectRouteParams } from '@app/core/core.state';
import { selectRouteParam } from '@app/core/core.state';
import { selectPreferencesPageSize } from '@app/core/preferences/preferences.selectors';
import { selectPreferencesImpact } from '@app/core/preferences/preferences.selectors';
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
import { actionNetworkMapViewInit } from './network.actions';
import { selectNetworkId } from './network.selectors';
import { selectNetworkChangesParameters } from './network.selectors';
import { selectNetworkMapPositionFromUrl } from './network.selectors';
import { selectNetworkMapPage } from './network.selectors';
import { NetworkMapService } from '@app/analysis/network/map/network-map.service';

@Injectable()
export class NetworkEffects {
  // noinspection JSUnusedGlobalSymbols
  networkDetailsPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionNetworkDetailsPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('networkId'))),
      map(([_, networkId]) =>
        actionNetworkDetailsPageLoad({ networkId: +networkId })
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  networkDetailsPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionNetworkDetailsPageLoad),
      mergeMap((action) => this.appService.networkDetails(action.networkId)),
      map((response) => actionNetworkDetailsPageLoaded(response))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  networkFactsPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionNetworkFactsPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('networkId'))),
      map(([_, networkId]) =>
        actionNetworkFactsPageLoad({ networkId: +networkId })
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  networkFactsPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionNetworkFactsPageLoad),
      mergeMap((action) => this.appService.networkFacts(action.networkId)),
      map((response) => actionNetworkFactsPageLoaded(response))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  networkNodesPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionNetworkNodesPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('networkId'))),
      map(([_, networkId]) =>
        actionNetworkNodesPageLoad({ networkId: +networkId })
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  networkNodesPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionNetworkNodesPageLoad),
      mergeMap((action) => this.appService.networkNodes(action.networkId)),
      map((response) => actionNetworkNodesPageLoaded(response))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  networkRoutesPage = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionNetworkRoutesPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('networkId'))),
      map(([_, networkId]) =>
        actionNetworkRoutesPageLoad({ networkId: +networkId })
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  networkRoutesPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionNetworkRoutesPageLoad),
      mergeMap((action) => this.appService.networkRoutes(action.networkId)),
      map((response) => actionNetworkRoutesPageLoaded(response))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  networkMapPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionNetworkMapPageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('networkId')),
        this.store.select(selectQueryParam('position')),
      ]),
      map(([_, networkId, mapPositionString]) => {
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
    );
  });

  // noinspection JSUnusedGlobalSymbols
  networkMapPageLoad = createEffect(() => {
    return this.actions$.pipe(
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
    );
  });

  // noinspection JSUnusedGlobalSymbols
  actionNetworkMapViewInit = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionNetworkMapViewInit),
        concatLatestFrom(() => [
          this.store.select(selectNetworkId),
          this.store.select(selectNetworkMapPage),
          this.store.select(selectNetworkMapPositionFromUrl),
        ]),
        tap(([_, networkId, response, mapPositionFromUrl]) => {
          this.networkMapService.init(
            networkId,
            response.result,
            mapPositionFromUrl
          );
        })
      );
    },
    {
      dispatch: false,
    }
  );

  // noinspection JSUnusedGlobalSymbols
  networkChangesPage = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionNetworkChangesPageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParams),
        this.store.select(selectQueryParams),
        this.store.select(selectPreferencesImpact),
        this.store.select(selectPreferencesPageSize),
      ]),
      map(
        ([
          _,
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
    );
  });

  // noinspection JSUnusedGlobalSymbols
  networkChangesPageLoad = createEffect(() => {
    return this.actions$.pipe(
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
      mergeMap(([_, networkId, changesParameters]) => {
        const promise = this.navigate(changesParameters);
        return from(promise).pipe(
          mergeMap(() =>
            this.appService.networkChanges(+networkId, changesParameters)
          ),
          map((response) => actionNetworkChangesPageLoaded(response))
        );
      })
    );
  });

  constructor(
    private actions$: Actions,
    private store: Store,
    private router: Router,
    private route: ActivatedRoute,
    private appService: AppService,
    private networkMapService: NetworkMapService
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
