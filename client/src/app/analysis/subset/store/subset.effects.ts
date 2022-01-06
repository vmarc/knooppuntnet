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
import { QueryParams } from '../../../base/query-params';
import { selectQueryParams } from '../../../core/core.state';
import { AppState } from '../../../core/core.state';
import { selectPreferencesPageSize } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
import { actionSubsetChangesPageLoaded } from './subset.actions';
import { actionSubsetChangesPageSize } from './subset.actions';
import { actionSubsetChangesPageImpact } from './subset.actions';
import { actionSubsetChangesPageLoad } from './subset.actions';
import { actionSubsetChangesFilterOption } from './subset.actions';
import { actionSubsetChangesPageIndex } from './subset.actions';
import { actionSubsetFactsPageInit } from './subset.actions';
import { actionSubsetOrphanRoutesPageInit } from './subset.actions';
import { actionSubsetOrphanNodesPageInit } from './subset.actions';
import { actionSubsetMapPageInit } from './subset.actions';
import { actionSubsetChangesPageInit } from './subset.actions';
import { actionSubsetNetworksPageInit } from './subset.actions';
import { actionSubsetMapPageLoaded } from './subset.actions';
import { actionSubsetOrphanRoutesPageLoaded } from './subset.actions';
import { actionSubsetOrphanNodesPageLoaded } from './subset.actions';
import { actionSubsetFactsPageLoaded } from './subset.actions';
import { actionSubsetNetworksPageLoaded } from './subset.actions';
import { selectSubsetChangesParameters } from './subset.selectors';
import { selectSubset } from './subset.selectors';

@Injectable()
export class SubsetEffects {
  networksPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetNetworksPageInit),
      concatLatestFrom(() => this.store.select(selectSubset)),
      mergeMap(([{}, subset]) => this.appService.subsetNetworks(subset)),
      map((response) => actionSubsetNetworksPageLoaded({ response }))
    )
  );

  factsPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetFactsPageInit),
      concatLatestFrom(() => this.store.select(selectSubset)),
      mergeMap(([{}, subset]) => this.appService.subsetFacts(subset)),
      map((response) => actionSubsetFactsPageLoaded({ response }))
    )
  );

  orphanNodesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetOrphanNodesPageInit),
      concatLatestFrom(() => this.store.select(selectSubset)),
      mergeMap(([{}, subset]) => this.appService.subsetOrphanNodes(subset)),
      map((response) => actionSubsetOrphanNodesPageLoaded({ response }))
    )
  );

  orphanRoutesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetOrphanRoutesPageInit),
      concatLatestFrom(() => this.store.select(selectSubset)),
      mergeMap(([{}, subset]) => this.appService.subsetOrphanRoutes(subset)),
      map((response) => actionSubsetOrphanRoutesPageLoaded({ response }))
    )
  );

  mapPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetMapPageInit),
      concatLatestFrom(() => this.store.select(selectSubset)),
      mergeMap(([{}, subset]) => this.appService.subsetMap(subset)),
      map((response) => actionSubsetMapPageLoaded({ response }))
    )
  );

  changesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetChangesPageInit),
      concatLatestFrom(() => [
        this.store.select(selectSubset),
        this.store.select(selectQueryParams),
        this.store.select(selectPreferencesImpact),
        this.store.select(selectPreferencesPageSize),
      ]),
      map(
        ([{}, subset, queryParams, preferencesImpact, preferencesPageSize]) => {
          const queryParamsWrapper = new QueryParams(queryParams);
          const changesParameters = queryParamsWrapper.changesParameters(
            preferencesImpact,
            preferencesPageSize
          );
          return actionSubsetChangesPageLoad({ subset, changesParameters });
        }
      )
    )
  );

  subsetChangesPageLoad = createEffect(() =>
    this.actions$.pipe(
      ofType(
        actionSubsetChangesPageLoad,
        actionSubsetChangesPageIndex,
        actionSubsetChangesPageImpact,
        actionSubsetChangesPageSize,
        actionSubsetChangesFilterOption
      ),
      concatLatestFrom(() => [
        this.store.select(selectSubset),
        this.store.select(selectSubsetChangesParameters),
      ]),
      mergeMap(([{}, subset, changesParameters]) => {
        const promise = this.navigate(changesParameters);
        return from(promise).pipe(
          mergeMap(() =>
            this.appService.subsetChanges(subset, changesParameters)
          ),
          map((response) => actionSubsetChangesPageLoaded({ response }))
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
