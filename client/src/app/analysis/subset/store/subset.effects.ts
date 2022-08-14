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
import { filter } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { PageParams } from '../../../base/page-params';
import { selectRouteParams } from '../../../core/core.state';
import { selectQueryParams } from '../../../core/core.state';
import { AppState } from '../../../core/core.state';
import { selectPreferencesPageSize } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
import { actionSharedEdit } from '../../../core/shared/shared.actions';
import { selectUserLoggedIn } from '../../../core/user/user.selectors';
import { EditParameters } from '../../components/edit/edit-parameters';
import { actionSubsetFactRefsLoaded } from './subset.actions';
import { actionSubsetFactRefsLoad } from './subset.actions';
import { actionSubsetOrphanNodesPageInit } from './subset.actions';
import { actionSubsetMapPageLoad } from './subset.actions';
import { actionSubsetOrphanRoutesPageInit } from './subset.actions';
import { actionSubsetOrphanRoutesPageLoad } from './subset.actions';
import { actionSubsetOrphanNodesPageLoad } from './subset.actions';
import { actionSubsetFactsPageLoad } from './subset.actions';
import { actionSubsetNetworksPageLoad } from './subset.actions';
import { actionSubsetFactDetailsPageLoad } from './subset.actions';
import { actionSubsetFactDetailsPageLoaded } from './subset.actions';
import { actionSubsetFactDetailsPageInit } from './subset.actions';
import { actionSubsetChangesPageLoaded } from './subset.actions';
import { actionSubsetChangesPageSize } from './subset.actions';
import { actionSubsetChangesPageImpact } from './subset.actions';
import { actionSubsetChangesPageLoad } from './subset.actions';
import { actionSubsetChangesFilterOption } from './subset.actions';
import { actionSubsetChangesPageIndex } from './subset.actions';
import { actionSubsetFactsPageInit } from './subset.actions';
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
  // noinspection JSUnusedGlobalSymbols
  networksPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetNetworksPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParams)),
      map(([{}, routeParams]) => {
        const subset = new PageParams(routeParams).subset();
        return actionSubsetNetworksPageLoad({ subset });
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  networksPageLoad = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetNetworksPageLoad),
      mergeMap((action) => this.appService.subsetNetworks(action.subset)),
      map((response) => actionSubsetNetworksPageLoaded({ response }))
    )
  );

  // noinspection JSUnusedGlobalSymbols
  factsPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetFactsPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParams)),
      map(([{}, routeParams]) => {
        const subset = new PageParams(routeParams).subset();
        return actionSubsetFactsPageLoad({ subset });
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  factsPageLoad = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetFactsPageLoad),
      mergeMap((action) => this.appService.subsetFacts(action.subset)),
      map((response) => actionSubsetFactsPageLoaded({ response }))
    )
  );

  // noinspection JSUnusedGlobalSymbols
  editFactRefsLoad = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetFactRefsLoad),
      concatLatestFrom(() => this.store.select(selectSubset)),
      mergeMap(([action, subset]) =>
        this.appService.subsetFactRefs(subset, action.fact)
      ),
      map((response) => actionSubsetFactRefsLoaded({ response }))
    )
  );

  // noinspection JSUnusedGlobalSymbols
  editDialog = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetFactRefsLoaded),
      map((action) => {
        const subsetFactRefs = action.response.result;
        let editParameters: EditParameters = null;
        if (subsetFactRefs.elementType === 'node') {
          editParameters = {
            nodeIds: subsetFactRefs.elementIds,
          };
        }
        if (subsetFactRefs.elementType === 'way') {
          editParameters = {
            wayIds: subsetFactRefs.elementIds,
          };
        }
        if (subsetFactRefs.elementType === 'relation') {
          editParameters = {
            relationIds: subsetFactRefs.elementIds,
            fullRelation: true,
          };
        }
        return actionSharedEdit({ editParameters });
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  factDetailsPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetFactDetailsPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParams)),
      map(([{}, routeParams]) => {
        const subsetFact = new PageParams(routeParams).subsetFact();
        return actionSubsetFactDetailsPageLoad({ subsetFact });
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  factDetailsPageLoad = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetFactDetailsPageLoad),
      mergeMap((action) =>
        this.appService.subsetFactDetails(
          action.subsetFact.subset,
          action.subsetFact.factName
        )
      ),
      map((response) => actionSubsetFactDetailsPageLoaded({ response }))
    )
  );

  // noinspection JSUnusedGlobalSymbols
  orphanNodesPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetOrphanNodesPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParams)),
      map(([{}, routeParams]) => {
        const subset = new PageParams(routeParams).subset();
        return actionSubsetOrphanNodesPageLoad({ subset });
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  orphanNodesPageLoad = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetOrphanNodesPageLoad),
      mergeMap((action) => this.appService.subsetOrphanNodes(action.subset)),
      map((response) => actionSubsetOrphanNodesPageLoaded({ response }))
    )
  );

  // noinspection JSUnusedGlobalSymbols
  orphanRoutesPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetOrphanRoutesPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParams)),
      map(([{}, routeParams]) => {
        const subset = new PageParams(routeParams).subset();
        return actionSubsetOrphanRoutesPageLoad({ subset });
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  orphanRoutesPageLoad = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetOrphanRoutesPageLoad),
      mergeMap((action) => this.appService.subsetOrphanRoutes(action.subset)),
      map((response) => actionSubsetOrphanRoutesPageLoaded({ response }))
    )
  );

  // noinspection JSUnusedGlobalSymbols
  mapPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetMapPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParams)),
      map(([{}, routeParams]) => {
        const subset = new PageParams(routeParams).subset();
        return actionSubsetMapPageLoad({ subset });
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  mapPageLoad = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetMapPageLoad),
      mergeMap((action) => this.appService.subsetMap(action.subset)),
      map((response) => actionSubsetMapPageLoaded({ response }))
    )
  );

  // noinspection JSUnusedGlobalSymbols
  changesPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSubsetChangesPageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParams),
        this.store.select(selectQueryParams),
        this.store.select(selectPreferencesImpact),
        this.store.select(selectPreferencesPageSize),
        this.store.select(selectUserLoggedIn),
      ]),
      filter(
        ([
          {},
          routeParams,
          queryParams,
          preferencesImpact,
          preferencesPageSize,
          loggedIn,
        ]) => loggedIn
      ),
      map(
        ([
          {},
          routeParams,
          queryParams,
          preferencesImpact,
          preferencesPageSize,
          loggedIn,
        ]) => {
          const pageParams = new PageParams(routeParams, queryParams);
          const subset = pageParams.subset();
          const changesParameters = pageParams.changesParameters(
            preferencesImpact,
            preferencesPageSize
          );
          return actionSubsetChangesPageLoad({ subset, changesParameters });
        }
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
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
