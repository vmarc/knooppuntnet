import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Params } from '@angular/router';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { from } from 'rxjs';
import { filter } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { withLatestFrom } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { AppState } from '../../../core/core.state';
import { actionPreferencesPageSize } from '../../../core/preferences/preferences.actions';
import { actionPreferencesImpact } from '../../../core/preferences/preferences.actions';
import { selectPreferencesPageSize } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
import { actionSubsetChangesFilterOption } from './subset.actions';
import { actionSubsetChangesPageIndex } from './subset.actions';
import { actionSubsetFactsPageInit } from './subset.actions';
import { actionSubsetOrphanRoutesPageInit } from './subset.actions';
import { actionSubsetOrphanNodesPageInit } from './subset.actions';
import { actionSubsetMapPageInit } from './subset.actions';
import { actionSubsetChangesPageInit } from './subset.actions';
import { actionSubsetNetworksPageInit } from './subset.actions';
import { actionSubsetChangesPageLoaded } from './subset.actions';
import { actionSubsetMapPageLoaded } from './subset.actions';
import { actionSubsetOrphanRoutesPageLoaded } from './subset.actions';
import { actionSubsetOrphanNodesPageLoaded } from './subset.actions';
import { actionSubsetFactsPageLoaded } from './subset.actions';
import { actionSubsetNetworksPageLoaded } from './subset.actions';
import { selectSubsetChangesParameters } from './subset.selectors';
import { selectSubsetChangesPage } from './subset.selectors';
import { selectSubset } from './subset.selectors';

@Injectable()
export class SubsetEffects {
  networksPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetNetworksPageInit),
      withLatestFrom(this.store.select(selectSubset)),
      mergeMap(([{}, subset]) =>
        this.appService
          .subsetNetworks(subset)
          .pipe(map((response) => actionSubsetNetworksPageLoaded({ response })))
      )
    )
  );

  factsPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetFactsPageInit),
      withLatestFrom(this.store.select(selectSubset)),
      mergeMap(([{}, subset]) =>
        this.appService
          .subsetFacts(subset)
          .pipe(map((response) => actionSubsetFactsPageLoaded({ response })))
      )
    )
  );

  orphanNodesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetOrphanNodesPageInit),
      withLatestFrom(this.store.select(selectSubset)),
      mergeMap(([{}, subset]) =>
        this.appService
          .subsetOrphanNodes(subset)
          .pipe(
            map((response) => actionSubsetOrphanNodesPageLoaded({ response }))
          )
      )
    )
  );

  orphanRoutesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetOrphanRoutesPageInit),
      withLatestFrom(this.store.select(selectSubset)),
      mergeMap(([{}, subset]) =>
        this.appService
          .subsetOrphanRoutes(subset)
          .pipe(
            map((response) => actionSubsetOrphanRoutesPageLoaded({ response }))
          )
      )
    )
  );

  mapPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetMapPageInit),
      withLatestFrom(this.store.select(selectSubset)),
      mergeMap(([{}, subset]) =>
        this.appService
          .subsetMap(subset)
          .pipe(map((response) => actionSubsetMapPageLoaded({ response })))
      )
    )
  );

  changesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetChangesPageInit),
      withLatestFrom(
        this.store.select(selectSubset),
        this.store.select(selectPreferencesImpact),
        this.store.select(selectPreferencesPageSize),
        this.store.select(selectSubsetChangesParameters)
      ),
      map(
        ([
          {},
          subset,
          preferencesImpact,
          preferencesPageSize,
          urlChangesParameters,
        ]) => {
          const pageSize =
            urlChangesParameters?.pageSize ?? preferencesPageSize;
          const pageIndex = urlChangesParameters?.pageIndex ?? 0;
          const impact = urlChangesParameters?.impact ?? preferencesImpact;
          const changesParameters: ChangesParameters = {
            ...urlChangesParameters,
            pageSize,
            pageIndex,
            impact,
          };
          return { subset, changesParameters };
        }
      ),
      tap(({ subset, changesParameters }) => this.navigate(changesParameters)),
      mergeMap(({ subset, changesParameters }) => {
        return this.appService
          .subsetChanges(subset, changesParameters)
          .pipe(map((response) => actionSubsetChangesPageLoaded({ response })));
      })
    )
  );

  subsetChangesPageUpdate = createEffect(() =>
    this.actions$.pipe(
      ofType(
        actionSubsetChangesPageIndex,
        actionPreferencesImpact,
        actionPreferencesPageSize,
        actionSubsetChangesFilterOption
      ),
      withLatestFrom(this.store.select(selectSubsetChangesPage)),
      // continue only if we are currently on the changes page!!
      filter(([{}, response]) => !!response),
      withLatestFrom(this.store.select(selectSubsetChangesParameters)),
      mergeMap(([[{}, {}], changesParameters]) => {
        const promise = this.navigate(changesParameters);
        return from(promise).pipe(map(actionSubsetChangesPageInit));
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
