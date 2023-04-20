import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { LocationChangesParameters } from '@api/common/location';
import { LocationNodesParameters } from '@api/common/location';
import { LocationRoutesParameters } from '@api/common/location';
import { AppService } from '@app/app.service';
import { selectRouteParam } from '@app/core';
import { selectSharedSurveyDateInfo } from '@app/core';
import { actionSharedSurveyDateInfoInit } from '@app/core';
import { AnalysisStrategy } from '@app/core';
import { actionPreferencesAnalysisStrategy } from '@app/core';
import { actionPreferencesPageSize } from '@app/core';
import { selectPreferencesPageSize } from '@app/core';
import { concatLatestFrom } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { from } from 'rxjs';
import { filter } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { LocationMapService } from '../map/location-map.service';
import { actionLocationSelectionPageInit } from './location.actions';
import { actionLocationRoutesPageSize } from './location.actions';
import { actionLocationNodesPageSize } from './location.actions';
import { actionLocationSelectionPageStrategy } from './location.actions';
import { actionLocationRoutesPageIndex } from './location.actions';
import { actionLocationRoutesType } from './location.actions';
import { actionLocationNodesPageIndex } from './location.actions';
import { actionLocationNodesType } from './location.actions';
import { actionLocationRoutesPageInit } from './location.actions';
import { actionLocationFactsPageInit } from './location.actions';
import { actionLocationMapPageInit } from './location.actions';
import { actionLocationChangesPageInit } from './location.actions';
import { actionLocationEditPageInit } from './location.actions';
import { actionLocationNodesPageInit } from './location.actions';
import { actionLocationRoutesPageLoaded } from './location.actions';
import { actionLocationFactsPageLoaded } from './location.actions';
import { actionLocationMapPageLoaded } from './location.actions';
import { actionLocationChangesPageLoaded } from './location.actions';
import { actionLocationEditPageLoaded } from './location.actions';
import { actionLocationNodesPageLoaded } from './location.actions';
import { actionLocationMapViewInit } from './location.actions';
import { selectLocationChangesPageIndex } from './location.selectors';
import { selectLocationRoutesType } from './location.selectors';
import { selectLocationRoutesPageIndex } from './location.selectors';
import { selectLocationNodesPageIndex } from './location.selectors';
import { selectLocationNodesType } from './location.selectors';
import { selectLocationKey } from './location.selectors';
import { selectLocationMapPage } from './location.selectors';

@Injectable()
export class LocationEffects {
  // noinspection JSUnusedGlobalSymbols
  analysisStrategy = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionLocationSelectionPageInit),
      map(() => {
        return actionPreferencesAnalysisStrategy({
          strategy: AnalysisStrategy.location,
        });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  locationSelectionPageStrategyChange = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionLocationSelectionPageStrategy),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('networkType')),
        this.store.select(selectRouteParam('country')),
      ]),
      mergeMap(([{ strategy }, networkType, country]) => {
        const url = `/analysis/${networkType}/${country}/networks`;
        const promise = this.router.navigate([url]);
        return from(promise).pipe(
          map(() => actionPreferencesAnalysisStrategy({ strategy }))
        );
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  locationNodesPage = createEffect(() => {
    return this.actions$.pipe(
      ofType(
        actionLocationNodesPageInit,
        actionLocationNodesType,
        actionLocationNodesPageSize,
        actionLocationNodesPageIndex
      ),
      concatLatestFrom(() => [
        this.store.select(selectLocationKey),
        this.store.select(selectLocationNodesType),
        this.store.select(selectPreferencesPageSize),
        this.store.select(selectLocationNodesPageIndex),
      ]),
      mergeMap(([_, locationKey, locationNodesType, pageSize, pageIndex]) => {
        const parameters: LocationNodesParameters = {
          locationNodesType,
          pageSize,
          pageIndex,
        };
        return this.appService.locationNodes(locationKey, parameters);
      }),
      map((response) => actionLocationNodesPageLoaded(response))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  pageSize = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionLocationNodesPageSize, actionLocationRoutesPageSize),
      map(({ pageSize }) => {
        return actionPreferencesPageSize({ pageSize });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  locationRoutesPage = createEffect(() => {
    return this.actions$.pipe(
      ofType(
        actionLocationRoutesPageInit,
        actionLocationRoutesType,
        actionLocationRoutesPageSize,
        actionLocationRoutesPageIndex
      ),
      concatLatestFrom(() => [
        this.store.select(selectLocationKey),
        this.store.select(selectLocationRoutesType),
        this.store.select(selectPreferencesPageSize),
        this.store.select(selectLocationRoutesPageIndex),
      ]),
      mergeMap(([_, locationKey, locationRoutesType, pageSize, pageIndex]) => {
        const parameters: LocationRoutesParameters = {
          locationRoutesType,
          pageSize,
          pageIndex,
        };
        return this.appService.locationRoutes(locationKey, parameters);
      }),
      map((response) => actionLocationRoutesPageLoaded(response))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  locationFactsPage = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionLocationFactsPageInit),
      concatLatestFrom(() => this.store.select(selectLocationKey)),
      mergeMap(([_, locationKey]) =>
        this.appService.locationFacts(locationKey)
      ),
      map((response) => actionLocationFactsPageLoaded(response))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  locationMapPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionLocationMapPageInit),
      map(() => actionSharedSurveyDateInfoInit())
    );
  });

  // noinspection JSUnusedGlobalSymbols
  locationMapPage = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionLocationMapPageInit),
      concatLatestFrom(() => this.store.select(selectLocationKey)),
      mergeMap(([_, locationKey]) => {
        return this.appService.locationMap(locationKey).pipe(
          map((response) => {
            return actionLocationMapPageLoaded({ response });
          })
        );
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  locationMapViewInit = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionLocationMapViewInit),
        concatLatestFrom(() => [
          this.store.select(selectLocationKey),
          this.store.select(selectLocationMapPage),
          this.store
            .select(selectSharedSurveyDateInfo)
            .pipe(filter((x) => x !== null)), // make sure surveyDateInfo is loaded
        ]),
        tap(([_, locationKey, response, surveyDateValues]) => {
          const geoJson = response.result.geoJson;
          const bounds = response.result.bounds;
          this.locationMapLayerService.init(
            locationKey.networkType,
            surveyDateValues,
            geoJson,
            bounds
          );
        })
      );
    },
    {
      dispatch: false,
    }
  );

  // noinspection JSUnusedGlobalSymbols
  locationChangesPage = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionLocationChangesPageInit),
      concatLatestFrom(() => [
        this.store.select(selectLocationKey),
        this.store.select(selectPreferencesPageSize),
        this.store.select(selectLocationChangesPageIndex),
      ]),
      mergeMap(([_, locationKey, pageSize, pageIndex]) => {
        const parameters: LocationChangesParameters = {
          pageSize,
          pageIndex,
        };
        return this.appService.locationChanges(locationKey, parameters);
      }),
      map((response) => actionLocationChangesPageLoaded(response))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  locationEditPage = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionLocationEditPageInit),
      concatLatestFrom(() => this.store.select(selectLocationKey)),
      mergeMap(([_, locationKey]) => this.appService.locationEdit(locationKey)),
      map((response) => actionLocationEditPageLoaded(response))
    );
  });

  constructor(
    private actions$: Actions,
    private store: Store,
    private router: Router,
    private route: ActivatedRoute,
    private appService: AppService,
    private locationMapLayerService: LocationMapService
  ) {}
}
