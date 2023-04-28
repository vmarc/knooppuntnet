import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { selectQueryParam } from '@app/core';
import { selectRouteParam } from '@app/core';
import { actionPreferencesPageSize } from '@app/core';
import { selectPreferencesPageSize } from '@app/core';
import { ApiService } from '@app/services';
import { concatLatestFrom } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { PoiMapService } from '../areas/poi-map.service';
import { PoiService } from '../poi.service';
import { actionLocationPoiSummaryLocationsLoaded } from './poi.actions';
import { actionLocationPoiSummaryCountryChanged } from './poi.actions';
import { actionLocationPoiSummaryPageLoaded } from './poi.actions';
import { actionLocationPoiSummaryPageInit } from './poi.actions';
import { actionLocationPoisPageIndex } from './poi.actions';
import { actionLocationPoisPageLoaded } from './poi.actions';
import { actionLocationPoisPageInit } from './poi.actions';
import { actionPoiAreasPageInit } from './poi.actions';
import { actionPoiAreasPageLoaded } from './poi.actions';
import { actionPoiAreasPageMapViewInit } from './poi.actions';
import { selectLocationPoisPageIndex } from './poi.selectors';
import { selectPoiAreasPage } from './poi.selectors';

@Injectable()
export class PoiEffects {
  // noinspection JSUnusedGlobalSymbols
  locationPoisPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(
        actionLocationPoisPageInit,
        actionPreferencesPageSize,
        actionLocationPoisPageIndex
      ),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('location')),
        this.store.select(selectQueryParam('layers')),
        this.store.select(selectPreferencesPageSize),
        this.store.select(selectLocationPoisPageIndex),
      ]),
      mergeMap(([_, location, layers, pageSize, pageIndex]) =>
        this.poiService
          .locationPois(location, layers, pageSize, pageIndex)
          .pipe(map((response) => actionLocationPoisPageLoaded(response)))
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  locationPoiSummaryPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionLocationPoiSummaryPageInit),
      concatLatestFrom(() => [this.store.select(selectRouteParam('location'))]),
      mergeMap(([_, location]) =>
        this.poiService
          .locationPoiSummary(location)
          .pipe(map((response) => actionLocationPoiSummaryPageLoaded(response)))
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  locationPoiSummaryCountryChanged = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionLocationPoiSummaryCountryChanged),
      mergeMap((action) =>
        this.poiService
          .locations(action.country)
          .pipe(
            map((response) => actionLocationPoiSummaryLocationsLoaded(response))
          )
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  poiAreasPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionPoiAreasPageInit),
      mergeMap(() =>
        this.apiService
          .poiAreas()
          .pipe(map((response) => actionPoiAreasPageLoaded(response)))
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  poiAreasPageMapViewInit = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionPoiAreasPageMapViewInit),
        concatLatestFrom(() => [this.store.select(selectPoiAreasPage)]),
        tap(([_, response]) => this.poiMapService.init(response.result))
      );
    },
    {
      dispatch: false,
    }
  );

  constructor(
    private actions$: Actions,
    private store: Store,
    private router: Router,
    private poiService: PoiService,
    private apiService: ApiService,
    private poiMapService: PoiMapService
  ) {}
}
