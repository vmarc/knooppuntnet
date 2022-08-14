import { LocationStrategy } from '@angular/common';
import { Location } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ofType } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { EditDialogComponent } from '../../analysis/components/edit/edit-dialog.component';
import { BrowserStorageService } from '../../services/browser-storage.service';
import { AppState } from '../core.state';
import { actionSharedLanguage } from './shared.actions';
import { actionSharedEdit } from './shared.actions';

@Injectable()
export class SharedEffects {
  constructor(
    private actions$: Actions,
    private dialog: MatDialog,
    private router: Router,
    private location: Location,
    private locationStrategy: LocationStrategy,
    private browserStorageService: BrowserStorageService,
    private http: HttpClient,
    private store: Store<AppState>
  ) {
    this.initLanguage();
  }

  // noinspection JSUnusedGlobalSymbols
  editDialog = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionSharedEdit),
        tap(({ editParameters }) => {
          if (editParameters) {
            this.dialog.open(EditDialogComponent, {
              data: editParameters,
              maxWidth: 600,
            });
          }
        })
      ),
    { dispatch: false }
  );

  private initLanguage() {
    const language = this.location.path().substring(1, 3);
    if (
      language === 'en' ||
      language === 'de' ||
      language === 'fr' ||
      language === 'nl'
    ) {
      this.store.dispatch(actionSharedLanguage({ language }));
    }
  }
}
