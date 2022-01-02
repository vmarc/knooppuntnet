import { Injectable } from '@angular/core';
import { Params } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { from } from 'rxjs';
import { map } from 'rxjs/operators';
import { withLatestFrom } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { QueryParams } from '../../../base/query-params';
import { selectQueryParams } from '../../../core/core.state';
import { AppState } from '../../../core/core.state';
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesAnalysisMode } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesPageSize } from '../../../core/preferences/preferences.selectors';
import { AnalysisMode } from '../../../core/preferences/preferences.state';
import { actionChangesAnalysisMode } from './changes.actions';
import { actionChangesPageSize } from './changes.actions';
import { actionChangesImpact } from './changes.actions';
import { actionChangesPageLoad } from './changes.actions';
import { actionChangesFilterOption } from './changes.actions';
import { actionChangesPageLoaded } from './changes.actions';
import { actionChangesPageIndex } from './changes.actions';
import { actionChangesPageInit } from './changes.actions';
import { selectChangesAnalysisMode } from './changes.selectors';
import { selectChangesParameters } from './changes.selectors';

@Injectable()
export class ChangesEffects {
  changesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionChangesPageInit),
      withLatestFrom(
        this.store.select(selectQueryParams),
        this.store.select(selectPreferencesAnalysisMode),
        this.store.select(selectPreferencesImpact),
        this.store.select(selectPreferencesPageSize)
      ),
      map(
        ([
          {},
          queryParams,
          preferencesAnalysisMode,
          preferencesImpact,
          preferencesPageSize,
        ]) => {
          let analysisMode: AnalysisMode = preferencesAnalysisMode;
          if (queryParams['analysisMode']) {
            analysisMode = queryParams['analysisMode'];
          }
          const queryParamsWrapper = new QueryParams(queryParams);
          const changesParameters = queryParamsWrapper.changesParameters(
            preferencesImpact,
            preferencesPageSize
          );
          return actionChangesPageLoad({ analysisMode, changesParameters });
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
        actionChangesAnalysisMode,
        actionChangesFilterOption
      ),
      withLatestFrom(
        this.store.select(selectChangesAnalysisMode),
        this.store.select(selectChangesParameters)
      ),
      mergeMap(([{}, analysisMode, changesParameters]) => {
        const promise = this.navigate(analysisMode, changesParameters);
        return from(promise).pipe(
          mergeMap(() => {
            return this.appService
              .changes(analysisMode, changesParameters)
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
    analysisMode: AnalysisMode,
    changesParameters: ChangesParameters
  ): Promise<boolean> {
    const queryParams: Params = {
      analysisMode,
      ...changesParameters,
    };
    return this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
    });
  }
}
