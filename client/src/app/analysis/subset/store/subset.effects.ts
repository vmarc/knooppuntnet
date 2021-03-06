import { Injectable } from '@angular/core';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { withLatestFrom } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { AppState } from '../../../core/core.state';
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
import { selectSubset } from './subset.selectors';

@Injectable()
export class SubsetEffects {
  networksPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetNetworksPageInit),
      withLatestFrom(this.store.select(selectSubset)),
      mergeMap(([action, subset]) =>
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
      mergeMap(([action, subset]) =>
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
      mergeMap(([action, subset]) =>
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
      mergeMap(([action, subset]) =>
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
      mergeMap(([action, subset]) =>
        this.appService
          .subsetMap(subset)
          .pipe(map((response) => actionSubsetMapPageLoaded({ response })))
      )
    )
  );

  changesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetChangesPageInit),
      withLatestFrom(this.store.select(selectSubset)),
      mergeMap(([action, subset]) => {
        const parameters: ChangesParameters = null;
        return this.appService
          .subsetChanges(subset, parameters)
          .pipe(map((response) => actionSubsetChangesPageLoaded({ response })));
      })
    )
  );

  constructor(
    private actions$: Actions,
    private store: Store<AppState>,
    private appService: AppService
  ) {}
}
