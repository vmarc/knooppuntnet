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
import { filter } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { PageParams } from '../../../base/page-params';
import { selectQueryParams } from '../../../core/core.state';
import { AppState } from '../../../core/core.state';
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesAnalysisStrategy } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesPageSize } from '../../../core/preferences/preferences.selectors';
import { AnalysisStrategy } from '../../../core/preferences/preferences.state';
import { selectUserLoggedIn } from '../../../core/user/user.selectors';
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
  changesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionChangesPageInit),
      concatLatestFrom(() => [
        this.store.select(selectQueryParams),
        this.store.select(selectPreferencesAnalysisStrategy),
        this.store.select(selectPreferencesImpact),
        this.store.select(selectPreferencesPageSize),
        this.store.select(selectUserLoggedIn),
      ]),
      filter(
        ([
          {},
          queryParams,
          preferencesAnalysisStrategy,
          preferencesImpact,
          preferencesPageSize,
          loggedIn,
        ]) => loggedIn
      ),
      map(
        ([
          {},
          queryParams,
          preferencesAnalysisStrategy,
          preferencesImpact,
          preferencesPageSize,
          loggedIn,
        ]) => {
          const pageParams = new PageParams(queryParams);
          const strategy = pageParams.strategy(preferencesAnalysisStrategy);
          const changesParameters = pageParams.changesParameters(
            preferencesImpact,
            preferencesPageSize
          );
          return actionChangesPageLoad({ strategy, changesParameters });
        }
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
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
          mergeMap(() => this.appService.changes(strategy, changesParameters)),
          map((response) => actionChangesPageLoaded({ response }))
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
