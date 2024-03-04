import { Location } from '@angular/common';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Params } from '@angular/router';
import { ChangesParameters } from '@api/common/changes/filter';
import { NetworkType } from '@api/custom';
import { Util } from '@app/components/shared';
import { PageParams } from '@app/shared/base';
import { MapPosition } from '@app/ol/domain';
import { selectQueryParam } from '@app/core';
import { selectQueryParams } from '@app/core';
import { selectRouteParams } from '@app/core';
import { selectRouteParam } from '@app/core';
import { actionPreferencesPageSize } from '@app/core';
import { actionPreferencesNetworkType } from '@app/core';
import { actionPreferencesImpact } from '@app/core';
import { selectPreferencesPageSize } from '@app/core';
import { selectPreferencesImpact } from '@app/core';
import { ApiService } from '@app/services';
import { concatLatestFrom } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { from } from 'rxjs';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { RouteMapService } from '../map/route-map.service';
import { actionRouteMapPageLoad } from './route.actions';
import { actionRouteDetailsPageLoad } from './route.actions';
import { actionRouteChangesPageLoaded } from './route.actions';
import { actionRouteChangesPageSize } from './route.actions';
import { actionRouteChangesPageImpact } from './route.actions';
import { actionRouteChangesPageLoad } from './route.actions';
import { actionRouteChangesFilterOption } from './route.actions';
import { actionRouteChangesPageIndex } from './route.actions';
import { actionRouteChangesPageInit } from './route.actions';
import { actionRouteMapPageInit } from './route.actions';
import { actionRouteDetailsPageInit } from './route.actions';
import { actionRouteDetailsPageLoaded } from './route.actions';
import { actionRouteMapPageLoaded } from './route.actions';
import { actionRouteMapViewInit } from './route.actions';
import { selectRouteNetworkType } from './route.selectors';
import { selectRouteChangesParameters } from './route.selectors';
import { selectRouteId } from './route.selectors';
import { selectRouteMapPage } from './route.selectors';
import { selectRouteMapPositionFromUrl } from './route.selectors';

@Injectable()
export class RouteEffects {
  private readonly actions$ = inject(Actions);
  private readonly store = inject(Store);
  private readonly apiService = inject(ApiService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly routeMapService = inject(RouteMapService);
  private readonly location = inject(Location);

  // noinspection JSUnusedGlobalSymbols
  routeDetailsInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionRouteDetailsPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('routeId'))),
      map(([_, routeId]) => {
        let routeName: string = undefined;
        let networkType: NetworkType = undefined;
        const state = this.location.getState();
        if (state) {
          routeName = state['routeName'];
          networkType = state['networkType'];
        }
        return actionRouteDetailsPageLoad({ routeId, routeName, networkType });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  routeDetailsLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionRouteDetailsPageLoad),
      mergeMap((action) => this.apiService.routeDetails(action.routeId)),
      map((response) => actionRouteDetailsPageLoaded(response))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  detailsPageLoadedNetworkType = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionRouteDetailsPageLoaded),
      concatLatestFrom(() => this.store.select(selectRouteNetworkType)),
      map(([response, currentNetworkType]) => {
        const networkType = response?.result?.route.summary.networkType ?? currentNetworkType;
        return actionPreferencesNetworkType({ networkType });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  routeMapInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionRouteMapPageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('routeId')),
        this.store.select(selectQueryParam('position')),
      ]),
      map(([_, routeId, mapPositionString]) => {
        const mapPositionFromUrl = MapPosition.fromQueryParam(mapPositionString);
        return actionRouteMapPageLoad({ routeId, mapPositionFromUrl });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  routeMapLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionRouteMapPageLoad),
      mergeMap((action) => {
        return this.apiService.routeMap(action.routeId).pipe(
          map((response) =>
            actionRouteMapPageLoaded({
              response,
              mapPositionFromUrl: action.mapPositionFromUrl,
            })
          )
        );
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  mapPageLoadedNetworkType = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionRouteMapPageLoaded),
      concatLatestFrom(() => this.store.select(selectRouteNetworkType)),
      map(([{ response }, currentNetworkType]) => {
        const networkType = response?.result?.routeMapInfo.networkType ?? currentNetworkType;
        return actionPreferencesNetworkType({ networkType });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  routeMapViewInit = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionRouteMapViewInit),
        concatLatestFrom(() => [
          this.store.select(selectRouteMapPage),
          this.store.select(selectRouteMapPositionFromUrl),
        ]),
        map(([_, response, mapPositionFromUrl]) => {
          this.routeMapService.init(response.result.routeMapInfo, mapPositionFromUrl);
        })
      );
    },
    {
      dispatch: false,
    }
  );

  // noinspection JSUnusedGlobalSymbols
  routeChanges = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionRouteChangesPageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParams),
        this.store.select(selectQueryParams),
        this.store.select(selectPreferencesImpact),
        this.store.select(selectPreferencesPageSize),
      ]),
      map(([_, routeParams, queryParams, preferencesImpact, preferencesPageSize]) => {
        const uniqueQueryParams = Util.uniqueParams(queryParams);
        const pageParams = new PageParams(routeParams, uniqueQueryParams);
        const routeId = pageParams.routeId();
        const changesParameters = pageParams.changesParameters(
          preferencesImpact,
          preferencesPageSize
        );
        return actionRouteChangesPageLoad({ routeId, changesParameters });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  routeChangesImpact = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionRouteChangesFilterOption),
      map(({ option }) => actionPreferencesImpact({ impact: option.impact }))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  pageSize = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionRouteChangesPageSize),
      map(({ pageSize }) => {
        return actionPreferencesPageSize({ pageSize });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  routeChangesPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(
        actionRouteChangesPageLoad,
        actionRouteChangesPageImpact,
        actionRouteChangesPageSize,
        actionRouteChangesPageIndex,
        actionRouteChangesFilterOption
      ),
      concatLatestFrom(() => [
        this.store.select(selectRouteId),
        this.store.select(selectRouteChangesParameters),
      ]),
      mergeMap(([_, routeId, changesParameters]) => {
        const promise = this.navigate(changesParameters);
        return from(promise).pipe(
          mergeMap(() => {
            return this.apiService.routeChanges(routeId, changesParameters);
          }),
          map((response) => actionRouteChangesPageLoaded(response))
        );
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  changesPageLoadedNetworkType = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionRouteChangesPageLoaded),
      concatLatestFrom(() => this.store.select(selectRouteNetworkType)),
      map(([response, currentNetworkType]) => {
        const networkType = response?.result?.routeNameInfo.networkType ?? currentNetworkType;
        return actionPreferencesNetworkType({ networkType });
      })
    );
  });

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
