import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { EditDialogComponent } from '@app/analysis/components/edit';
import { ApiService } from '@app/services';
import { concatLatestFrom } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { actionSharedSurveyDateInfoLoaded } from './shared.actions';
import { actionSharedSurveyDateInfoAlreadyLoaded } from './shared.actions';
import { actionSharedSurveyDateInfoInit } from './shared.actions';
import { actionSharedEdit } from './shared.actions';
import { selectSharedSurveyDateInfo } from './shared.selectors';
import { SurveyDateValues } from './survey-date-values';

@Injectable()
export class SharedEffects {
  constructor(
    private actions$: Actions,
    private dialog: MatDialog,
    private store: Store,
    private apiService: ApiService
  ) {}

  // noinspection JSUnusedGlobalSymbols
  editDialog = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionSharedEdit),
        tap((editParameters) => {
          if (editParameters) {
            this.dialog.open(EditDialogComponent, {
              data: editParameters,
              autoFocus: false,
              maxWidth: 600,
            });
          }
        })
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  surveyDateInfoInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionSharedSurveyDateInfoInit),
      concatLatestFrom(() => this.store.select(selectSharedSurveyDateInfo)),
      mergeMap(([_, currentSurveyDateInfo]) => {
        if (!!currentSurveyDateInfo) {
          return of(actionSharedSurveyDateInfoAlreadyLoaded());
        }
        return this.apiService.surveyDateInfo().pipe(
          map((response) => {
            const surveyDateInfo = SurveyDateValues.from(response.result);
            return actionSharedSurveyDateInfoLoaded({ surveyDateInfo });
          })
        );
      })
    );
  });
}
