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
import { selectQueryParam } from '../../../core/core.state';
import { selectQueryParams } from '../../../core/core.state';
import { selectRouteParams } from '../../../core/core.state';
import { selectRouteParam } from '../../../core/core.state';
import { AppState } from '../../../core/core.state';
import { selectPreferencesPageSize } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
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
import { selectRouteChangesParameters } from './route.selectors';
import { selectRouteId } from './route.selectors';

@Injectable()
export class RouteEffects {
  // noinspection JSUnusedGlobalSymbols
  routeDetailsInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionRouteDetailsPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('routeId'))),
      map(([{}, routeId]) => actionRouteDetailsPageLoad({ routeId }))
    )
  );

  // noinspection JSUnusedGlobalSymbols
  routeDetailsLoad = createEffect(() =>
    this.actions$.pipe(
      ofType(actionRouteDetailsPageLoad),
      mergeMap((action) => this.appService.routeDetails(action.routeId)),
      map((response) => actionRouteDetailsPageLoaded({ response }))
    )
  );

  // noinspection JSUnusedGlobalSymbols
  routeMapInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionRouteMapPageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('routeId')),
        this.store.select(selectQueryParam('position')),
      ]),
      map(([{}, routeId, mapPositionString]) => {
        const mapPositionFromUrl =
          MapPosition.fromQueryParam(mapPositionString);
        return actionRouteMapPageLoad({ routeId, mapPositionFromUrl });
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  routeMapLoad = createEffect(() =>
    this.actions$.pipe(
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
    )
  );

  // noinspection JSUnusedGlobalSymbols
  routeChanges = createEffect(() =>
    this.actions$.pipe(
      ofType(actionRouteChangesPageInit),
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
          const routeId = pageParams.routeId();
          const changesParameters = pageParams.changesParameters(
            preferencesImpact,
            preferencesPageSize
          );
          return actionRouteChangesPageLoad({ routeId, changesParameters });
        }
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  routeChangesPageLoad = createEffect(() =>
    this.actions$.pipe(
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
      mergeMap(([{}, routeId, changesParameters]) => {
        const promise = this.navigate(changesParameters);
        return from(promise).pipe(
          mergeMap(() => {
            return this.appService.routeChanges(routeId, changesParameters);
          }),
          map((response) => actionRouteChangesPageLoaded({ response }))
        );
      })
    )
  );

  constructor(
    private actions$: Actions,
    private store: Store<AppState>,
    private appService: AppService,
    private router: Router,
    private route: ActivatedRoute
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
