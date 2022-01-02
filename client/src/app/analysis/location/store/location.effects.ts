import { Injectable } from '@angular/core';
import { LocationChangesParameters } from '@api/common/location/location-changes-parameters';
import { LocationNodesParameters } from '@api/common/location/location-nodes-parameters';
import { LocationRoutesParameters } from '@api/common/location/location-routes-parameters';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { withLatestFrom } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { AppState } from '../../../core/core.state';
import { selectPreferencesPageSize } from '../../../core/preferences/preferences.selectors';
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
  locationNodesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(
        actionLocationNodesPageInit,
        actionLocationNodesType,
        actionLocationNodesPageIndex
      ),
      withLatestFrom(
        this.store.select(selectLocationKey),
        this.store.select(selectLocationNodesType),
        this.store.select(selectPreferencesPageSize),
        this.store.select(selectLocationNodesPageIndex)
      ),
      mergeMap(
        ([action, locationKey, locationNodesType, pageSize, pageIndex]) => {
          const parameters: LocationNodesParameters = {
            locationNodesType,
            pageSize,
            pageIndex,
          };
          return this.appService
            .locationNodes(locationKey, parameters)
            .pipe(
              map((response) => actionLocationNodesPageLoaded({ response }))
            );
        }
      )
    )
  );

  locationRoutesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(
        actionLocationRoutesPageInit,
        actionLocationRoutesType,
        actionLocationRoutesPageIndex
      ),
      withLatestFrom(
        this.store.select(selectLocationKey),
        this.store.select(selectLocationRoutesType),
        this.store.select(selectPreferencesPageSize),
        this.store.select(selectLocationRoutesPageIndex)
      ),
      mergeMap(
        ([action, locationKey, locationRoutesType, pageSize, pageIndex]) => {
          const parameters: LocationRoutesParameters = {
            locationRoutesType,
            pageSize,
            pageIndex,
          };
          return this.appService
            .locationRoutes(locationKey, parameters)
            .pipe(
              map((response) => actionLocationRoutesPageLoaded({ response }))
            );
        }
      )
    )
  );

  locationFactsPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionLocationFactsPageInit),
      withLatestFrom(this.store.select(selectLocationKey)),
      mergeMap(([action, locationKey]) => {
        return this.appService
          .locationFacts(locationKey)
          .pipe(map((response) => actionLocationFactsPageLoaded({ response })));
      })
    )
  );

  locationMapPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionLocationMapPageInit),
      withLatestFrom(this.store.select(selectLocationKey)),
      mergeMap(([action, locationKey]) => {
        return this.appService
          .locationMap(locationKey)
          .pipe(map((response) => actionLocationMapPageLoaded({ response })));
      })
    )
  );

  locationChangesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionLocationChangesPageInit),
      withLatestFrom(
        this.store.select(selectLocationKey),
        this.store.select(selectPreferencesPageSize),
        this.store.select(selectLocationChangesPageIndex)
      ),
      mergeMap(([action, locationKey, pageSize, pageIndex]) => {
        const parameters: LocationChangesParameters = {
          pageSize,
          pageIndex,
        };
        return this.appService
          .locationChanges(locationKey, parameters)
          .pipe(
            map((response) => actionLocationChangesPageLoaded({ response }))
          );
      })
    )
  );

  locationEditPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionLocationEditPageInit),
      withLatestFrom(this.store.select(selectLocationKey)),
      mergeMap(([action, locationKey]) => {
        return this.appService
          .locationEdit(locationKey)
          .pipe(map((response) => actionLocationEditPageLoaded({ response })));
      })
    )
  );

  constructor(
    private actions$: Actions,
    private store: Store<AppState>,
    private appService: AppService
  ) {}
}
