import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Params } from '@angular/router';
import { ChangesParameters } from '@api/common/changes/filter';
import { EditParameters } from '@app/analysis/components/edit';
import { PageParams } from '@app/shared/base';
import { EditService } from '@app/components/shared';
import { selectRouteParams } from '@app/core';
import { selectQueryParams } from '@app/core';
import { actionPreferencesPageSize } from '@app/core';
import { actionPreferencesImpact } from '@app/core';
import { selectPreferencesPageSize } from '@app/core';
import { selectPreferencesImpact } from '@app/core';
import { ApiService } from '@app/services';
import { concatLatestFrom } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { from } from 'rxjs';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { SubsetMapNetworkDialogComponent } from '../map/subset-map-network-dialog.component';
import { SubsetMapService } from '../map/subset-map.service';
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
import { actionSubsetMapPageNetworkClicked } from './subset.actions';
import { actionSubsetMapViewInit } from './subset.actions';
import { selectSubsetChangesParameters } from './subset.selectors';
import { selectSubset } from './subset.selectors';
import { selectSubsetMapPage } from './subset.selectors';

@Injectable()
export class SubsetEffects {
  private readonly actions$ = inject(Actions);
  private readonly store = inject(Store);
  private readonly apiService = inject(ApiService);
  private readonly editService = inject(EditService);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly subsetMapService = inject(SubsetMapService);
  private readonly dialog = inject(MatDialog);

  // noinspection JSUnusedGlobalSymbols
  networksPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionSubsetNetworksPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParams)),
      map(([_, routeParams]) => {
        const subset = new PageParams(routeParams).subset();
        return actionSubsetNetworksPageLoad({ subset });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  networksPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionSubsetNetworksPageLoad),
      mergeMap((action) => this.apiService.subsetNetworks(action.subset)),
      map((response) => actionSubsetNetworksPageLoaded(response))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  factsPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionSubsetFactsPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParams)),
      map(([_, routeParams]) => {
        const subset = new PageParams(routeParams).subset();
        return actionSubsetFactsPageLoad({ subset });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  factsPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionSubsetFactsPageLoad),
      mergeMap((action) => this.apiService.subsetFacts(action.subset)),
      map((response) => actionSubsetFactsPageLoaded(response))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  editFactRefsLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionSubsetFactRefsLoad),
      concatLatestFrom(() => this.store.select(selectSubset)),
      mergeMap(([action, subset]) => this.apiService.subsetFactRefs(subset, action.fact)),
      map((response) => actionSubsetFactRefsLoaded(response))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  editDialog = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionSubsetFactRefsLoaded),
        map((response) => {
          const subsetFactRefs = response.result;
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
          this.editService.edit(editParameters);
        })
      );
    },
    {
      dispatch: false,
    }
  );

  // noinspection JSUnusedGlobalSymbols
  factDetailsPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionSubsetFactDetailsPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParams)),
      map(([_, routeParams]) => {
        const subsetFact = new PageParams(routeParams).subsetFact();
        return actionSubsetFactDetailsPageLoad({ subsetFact });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  factDetailsPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionSubsetFactDetailsPageLoad),
      mergeMap((action) =>
        this.apiService.subsetFactDetails(action.subsetFact.subset, action.subsetFact.factName)
      ),
      map((response) => actionSubsetFactDetailsPageLoaded(response))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  orphanNodesPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionSubsetOrphanNodesPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParams)),
      map(([_, routeParams]) => {
        const subset = new PageParams(routeParams).subset();
        return actionSubsetOrphanNodesPageLoad({ subset });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  orphanNodesPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionSubsetOrphanNodesPageLoad),
      mergeMap((action) => this.apiService.subsetOrphanNodes(action.subset)),
      map((response) => actionSubsetOrphanNodesPageLoaded(response))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  orphanRoutesPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionSubsetOrphanRoutesPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParams)),
      map(([_, routeParams]) => {
        const subset = new PageParams(routeParams).subset();
        return actionSubsetOrphanRoutesPageLoad({ subset });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  orphanRoutesPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionSubsetOrphanRoutesPageLoad),
      mergeMap((action) => this.apiService.subsetOrphanRoutes(action.subset)),
      map((response) => actionSubsetOrphanRoutesPageLoaded(response))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  mapPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionSubsetMapPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParams)),
      map(([_, routeParams]) => {
        const subset = new PageParams(routeParams).subset();
        return actionSubsetMapPageLoad({ subset });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  mapPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionSubsetMapPageLoad),
      mergeMap((action) => this.apiService.subsetMap(action.subset)),
      map((response) => actionSubsetMapPageLoaded(response))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  mapViewInit = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionSubsetMapViewInit),
        concatLatestFrom(() => [this.store.select(selectSubsetMapPage)]),
        tap(([_, response]) =>
          this.subsetMapService.init(response.result.networks, response.result.bounds)
        )
      );
    },
    {
      dispatch: false,
    }
  );

  // noinspection JSUnusedGlobalSymbols
  subsetMapPageNetworkClicked = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionSubsetMapPageNetworkClicked),
        concatLatestFrom(() => [this.store.select(selectSubsetMapPage)]),
        tap(([{ networkId }, response]) => {
          const network = response.result.networks.find((n) => n.id === networkId);
          if (network) {
            this.dialog.open(SubsetMapNetworkDialogComponent, {
              data: network,
              autoFocus: false,
              maxWidth: 600,
            });
          }
        })
      );
    },
    {
      dispatch: false,
    }
  );

  // noinspection JSUnusedGlobalSymbols
  changesPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionSubsetChangesPageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParams),
        this.store.select(selectQueryParams),
        this.store.select(selectPreferencesImpact),
        this.store.select(selectPreferencesPageSize),
      ]),
      map(([_, routeParams, queryParams, preferencesImpact, preferencesPageSize]) => {
        const pageParams = new PageParams(routeParams, queryParams);
        const subset = pageParams.subset();
        const changesParameters = pageParams.changesParameters(
          preferencesImpact,
          preferencesPageSize
        );
        return actionSubsetChangesPageLoad({ subset, changesParameters });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  subsetChangesImpact = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionSubsetChangesPageImpact),
      map(({ impact }) => actionPreferencesImpact({ impact }))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  pageSize = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionSubsetChangesPageSize),
      map(({ pageSize }) => {
        return actionPreferencesPageSize({ pageSize });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  subsetChangesPageLoad = createEffect(() => {
    return this.actions$.pipe(
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
      mergeMap(([_, subset, changesParameters]) => {
        const promise = this.navigate(changesParameters);
        return from(promise).pipe(
          mergeMap(() => this.apiService.subsetChanges(subset, changesParameters)),
          map((response) => actionSubsetChangesPageLoaded(response))
        );
      })
    );
  });

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
