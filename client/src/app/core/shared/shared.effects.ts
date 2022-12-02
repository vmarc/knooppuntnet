import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ofType } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { tap } from 'rxjs/operators';
import { EditDialogComponent } from '../../analysis/components/edit/edit-dialog.component';
import { actionSharedEdit } from './shared.actions';

@Injectable()
export class SharedEffects {
  constructor(private actions$: Actions, private dialog: MatDialog) {}

  // noinspection JSUnusedGlobalSymbols
  editDialog = createEffect(
    () =>
      this.actions$.pipe(
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
      ),
    { dispatch: false }
  );
}
