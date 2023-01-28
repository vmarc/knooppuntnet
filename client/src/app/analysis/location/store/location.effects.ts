import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { LocationChangesParameters } from '@api/common/location/location-changes-parameters';
import { LocationNodesParameters } from '@api/common/location/location-nodes-parameters';
import { LocationRoutesParameters } from '@api/common/location/location-routes-parameters';
import { concatLatestFrom } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { selectRouteParam } from '../../../core/core.state';
import { selectPreferencesPageSize } from '../../../core/preferences/preferences.selectors';
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
import { selectLocationChangesPageIndex } from './location.selectors';
import { selectLocationRoutesType } from './location.selectors';
import { selectLocationRoutesPageIndex } from './location.selectors';
import { selectLocationNodesPageIndex } from './location.selectors';
import { selectLocationNodesType } from './location.selectors';
import { selectLocationKey } from './location.selectors';

@Injectable()
export class LocationEffects {
  // noinspection JSUnusedGlobalSymbols
  locationSelectionPageStrategyChange = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionLocationSelectionPageStrategy),
        concatLatestFrom(() => [
          this.store.select(selectRouteParam('networkType')),
          this.store.select(selectRouteParam('country')),
        ]),
        tap(([_, networkType, country]) => {
          const url = `/analysis/${networkType}/${country}/networks`;
          this.router.navigate([url]);
        })
      );
    },
    { dispatch: false }
  );

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
  locationMapPage = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionLocationMapPageInit),
      concatLatestFrom(() => this.store.select(selectLocationKey)),
      mergeMap(([_, locationKey]) => this.appService.locationMap(locationKey)),
      map((response) => actionLocationMapPageLoaded(response))
    );
  });

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
    private appService: AppService
  ) {}
}
