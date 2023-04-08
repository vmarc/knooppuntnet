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
import { AppService } from '@app/app.service';
import { PageParams } from '@app/base/page-params';
import { MapPosition } from '@app/components/ol/domain/map-position';
import { selectQueryParam } from '@app/core/core.state';
import { selectQueryParams } from '@app/core/core.state';
import { selectRouteParams } from '@app/core/core.state';
import { selectRouteParam } from '@app/core/core.state';
import { selectPreferencesPageSize } from '@app/core/preferences/preferences.selectors';
import { selectPreferencesImpact } from '@app/core/preferences/preferences.selectors';
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
import { selectRouteChangesParameters } from './route.selectors';
import { selectRouteId } from './route.selectors';
import { selectRouteMapPage } from './route.selectors';
import { selectRouteMapPositionFromUrl } from './route.selectors';
import { RouteMapService } from '@app/analysis/route/map/route-map.service';

@Injectable()
export class RouteEffects {
  // noinspection JSUnusedGlobalSymbols
  routeDetailsInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionRouteDetailsPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('routeId'))),
      map(([_, routeId]) => actionRouteDetailsPageLoad({ routeId }))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  routeDetailsLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionRouteDetailsPageLoad),
      mergeMap((action) => this.appService.routeDetails(action.routeId)),
      map((response) => actionRouteDetailsPageLoaded(response))
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
        const mapPositionFromUrl =
          MapPosition.fromQueryParam(mapPositionString);
        return actionRouteMapPageLoad({ routeId, mapPositionFromUrl });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  routeMapLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionRouteMapPageLoad),
      mergeMap((action) => {
        return this.appService.routeMap(action.routeId).pipe(
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
  routeMapViewInit = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionRouteMapViewInit),
        concatLatestFrom(() => [
          this.store.select(selectRouteMapPage),
          this.store.select(selectRouteMapPositionFromUrl),
        ]),
        map(([_, response, mapPositionFromUrl]) => {
          this.routeMapService.init(
            response.result.routeMapInfo,
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
  routeChanges = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionRouteChangesPageInit),
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
          const routeId = pageParams.routeId();
          const changesParameters = pageParams.changesParameters(
            preferencesImpact,
            preferencesPageSize
          );
          return actionRouteChangesPageLoad({ routeId, changesParameters });
        }
      )
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
            return this.appService.routeChanges(routeId, changesParameters);
          }),
          map((response) => actionRouteChangesPageLoaded(response))
        );
      })
    );
  });

  constructor(
    private actions$: Actions,
    private store: Store,
    private appService: AppService,
    private router: Router,
    private route: ActivatedRoute,
    private routeMapService: RouteMapService
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
