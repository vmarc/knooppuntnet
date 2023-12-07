import { Injectable } from '@angular/core';
import { Params } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { ChangesParameters } from '@api/common/changes/filter';
import { PageParams } from '@app/shared/base';
import { selectQueryParams } from '@app/core';
import { actionPreferencesAnalysisStrategy } from '@app/core';
import { actionPreferencesPageSize } from '@app/core';
import { actionPreferencesImpact } from '@app/core';
import { selectPreferencesImpact } from '@app/core';
import { selectPreferencesAnalysisStrategy } from '@app/core';
import { selectPreferencesPageSize } from '@app/core';
import { AnalysisStrategy } from '@app/core';
import { ApiService } from '@app/services';
import { concatLatestFrom } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { from } from 'rxjs';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { actionChangesAnalysisStrategy } from './changes.actions';
import { actionChangesPageSize } from './changes.actions';
import { actionChangesImpact } from './changes.actions';
import { actionChangesPageLoad } from './changes.actions';
import { actionChangesFilterOption } from './changes.actions';
import { actionChangesPageLoaded } from './changes.actions';
import { actionChangesPageIndex } from './changes.actions';
import { actionChangesPageInit } from './changes.actions';
import { selectChangesParameters } from './changes.selectors';

@Injectable()
export class ChangesEffects {
  // noinspection JSUnusedGlobalSymbols
  changesPage = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionChangesPageInit),
      concatLatestFrom(() => [
        this.store.select(selectQueryParams),
        this.store.select(selectPreferencesAnalysisStrategy),
        this.store.select(selectPreferencesImpact),
        this.store.select(selectPreferencesPageSize),
      ]),
      map(
        ([_, queryParams, preferencesAnalysisStrategy, preferencesImpact, preferencesPageSize]) => {
          const pageParams = new PageParams(queryParams);
          const strategy = pageParams.strategy(preferencesAnalysisStrategy);
          const changesParameters = pageParams.changesParameters(
            preferencesImpact,
            preferencesPageSize
          );
          return actionChangesPageLoad({ strategy, changesParameters });
        }
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  changesImpact = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionChangesImpact),
      map(({ impact }) => actionPreferencesImpact({ impact }))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  pageSize = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionChangesPageSize),
      map(({ pageSize }) => {
        return actionPreferencesPageSize({ pageSize });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  analysisStrategy = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionChangesAnalysisStrategy),
      map(({ strategy }) => {
        return actionPreferencesAnalysisStrategy({ strategy });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  changesPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(
        actionChangesPageLoad,
        actionChangesImpact,
        actionChangesPageSize,
        actionChangesPageIndex,
        actionChangesAnalysisStrategy,
        actionChangesFilterOption
      ),
      concatLatestFrom(() => [
        this.store.select(selectPreferencesAnalysisStrategy),
        this.store.select(selectChangesParameters),
      ]),
      mergeMap(([action, preferredStrategy, changesParameters]) => {
        let strategy = preferredStrategy;
        if (action['strategy']) {
          strategy = action['strategy'];
        }
        const promise = this.navigate(strategy, changesParameters);
        return from(promise).pipe(
          mergeMap(() => this.apiService.changes(strategy, changesParameters)),
          map((response) => actionChangesPageLoaded(response))
        );
      })
    );
  });

  constructor(
    private actions$: Actions,
    private store: Store,
    private router: Router,
    private route: ActivatedRoute,
    private apiService: ApiService
  ) {}

  private navigate(
    strategy: AnalysisStrategy,
    changesParameters: ChangesParameters
  ): Promise<boolean> {
    const queryParams: Params = {
      strategy,
      ...changesParameters,
    };
    return this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
    });
  }
}
