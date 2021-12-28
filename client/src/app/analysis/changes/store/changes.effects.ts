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
import { tap } from 'rxjs/operators';
import { filter } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { withLatestFrom } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { AppState } from '../../../core/core.state';
import { actionPreferencesItemsPerPage } from '../../../core/preferences/preferences.actions';
import { actionPreferencesAnalysisMode } from '../../../core/preferences/preferences.actions';
import { actionPreferencesImpact } from '../../../core/preferences/preferences.actions';
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesAnalysisMode } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesItemsPerPage } from '../../../core/preferences/preferences.selectors';
import { AnalysisMode } from '../../../core/preferences/preferences.state';
import { actionChangesFilterOption } from './changes.actions';
import { actionChangesPageLoaded } from './changes.actions';
import { actionChangesPageIndex } from './changes.actions';
import { actionChangesPageInit } from './changes.actions';
import { selectChangesAnalysisMode } from './changes.selectors';
import { selectChangesParameters } from './changes.selectors';
import { selectChangesPage } from './changes.selectors';

@Injectable()
export class ChangesEffects {
  changesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionChangesPageInit),
      withLatestFrom(
        this.store.select(selectPreferencesAnalysisMode),
        this.store.select(selectPreferencesImpact),
        this.store.select(selectPreferencesItemsPerPage),
        this.store.select(selectChangesAnalysisMode),
        this.store.select(selectChangesParameters)
      ),
      map(
        ([
          action,
          preferencesAnalysisMode,
          preferencesImpact,
          preferencesItemsPerPage,
          urlAnalysisMode,
          urlChangesParameters,
        ]) => {
          let analysisMode: AnalysisMode = urlAnalysisMode;
          if (!analysisMode) {
            analysisMode = preferencesAnalysisMode;
          }

          let itemsPerPage: number = preferencesItemsPerPage;
          if (urlChangesParameters?.itemsPerPage) {
            itemsPerPage = +urlChangesParameters?.itemsPerPage;
          }
          const pageIndex: number = urlChangesParameters?.pageIndex ?? 0;
          const impact = urlChangesParameters?.impact ?? preferencesImpact;
          const changesParameters: ChangesParameters = {
            ...urlChangesParameters,
            itemsPerPage,
            pageIndex,
            impact,
          };

          return {
            analysisMode,
            changesParameters,
          };
        }
      ),
      tap(({ analysisMode, changesParameters }) => {
        this.navigate(analysisMode, changesParameters);
      }),
      mergeMap(({ analysisMode, changesParameters }) =>
        this.appService
          .changes(analysisMode, changesParameters)
          .pipe(map((response) => actionChangesPageLoaded({ response })))
      )
    )
  );

  changesPageUpdate$ = createEffect(() =>
    this.actions$.pipe(
      ofType(
        actionChangesPageIndex,
        actionPreferencesImpact,
        actionPreferencesItemsPerPage,
        actionPreferencesAnalysisMode,
        actionChangesFilterOption
      ),
      withLatestFrom(this.store.select(selectChangesPage)),
      // continue only if we are currently on the changes page!!
      filter(([action, response]) => !!response),
      withLatestFrom(
        this.store.select(selectChangesAnalysisMode),
        this.store.select(selectChangesParameters)
      ),
      mergeMap(([[action, response], analysisMode, changesParameters]) => {
        const promise = this.navigate(analysisMode, changesParameters);
        return from(promise).pipe(map(actionChangesPageInit));
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
