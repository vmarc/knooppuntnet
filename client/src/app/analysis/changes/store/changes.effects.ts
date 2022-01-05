import { Injectable } from '@angular/core';
import { Params } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
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
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesAnalysisStrategy } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesPageSize } from '../../../core/preferences/preferences.selectors';
import { AnalysisStrategy } from '../../../core/preferences/preferences.state';
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
  changesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionChangesPageInit),
      concatLatestFrom(() => [
        this.store.select(selectQueryParams),
        this.store.select(selectPreferencesAnalysisStrategy),
        this.store.select(selectPreferencesImpact),
        this.store.select(selectPreferencesPageSize),
      ]),
      map(
        ([
          {},
          queryParams,
          preferencesAnalysisStrategy,
          preferencesImpact,
          preferencesPageSize,
        ]) => {
          let strategy: AnalysisStrategy = preferencesAnalysisStrategy;
          if (queryParams['strategy']) {
            strategy = queryParams['strategy'];
          }
          const queryParamsWrapper = new QueryParams(queryParams);
          const changesParameters = queryParamsWrapper.changesParameters(
            preferencesImpact,
            preferencesPageSize
          );
          return actionChangesPageLoad({ strategy, changesParameters });
        }
      )
    )
  );

  changesPageLoad = createEffect(() =>
    this.actions$.pipe(
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
      mergeMap(([{}, strategy, changesParameters]) => {
        const promise = this.navigate(strategy, changesParameters);
        return from(promise).pipe(
          mergeMap(() => {
            return this.appService
              .changes(strategy, changesParameters)
              .pipe(map((response) => actionChangesPageLoaded({ response })));
          })
        );
      })
    )
  );

  constructor(
    private actions$: Actions,
    private store: Store<AppState>,
    private router: Router,
    private route: ActivatedRoute,
    private appService: AppService
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
