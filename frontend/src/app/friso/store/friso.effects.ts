import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Params } from '@angular/router';
import { selectQueryParam } from '@app/core';
import { concatLatestFrom } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { from } from 'rxjs';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { FrisoMapService } from '../friso/friso-map.service';
import { FrisoNodeDialogComponent } from '../friso/friso-node-dialog.component';
import { actionFrisoMode } from './friso.actions';
import { actionFrisoMapInitialized } from './friso.actions';
import { actionFrisoNodeClicked } from './friso.actions';
import { actionFrisoMapViewInit } from './friso.actions';

@Injectable()
export class FrisoEffects {
  private readonly actions$ = inject(Actions);
  private readonly store = inject(Store);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly dialog = inject(MatDialog);
  private readonly frisoMapService = inject(FrisoMapService);

  // noinspection JSUnusedGlobalSymbols
  mapViewInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionFrisoMapViewInit),
      concatLatestFrom(() => this.store.select(selectQueryParam('mode'))),
      mergeMap(([_, modeParam]) => {
        let mode = 'rename';
        if (!!modeParam) {
          mode = modeParam;
        }
        return this.navigate(mode).pipe(
          map(() => {
            this.frisoMapService.init(mode);
            return actionFrisoMapInitialized({ mode });
          })
        );
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  frisoMode = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionFrisoMode),
        map(({ mode }) => {
          this.frisoMapService.updateMode(mode);
          this.navigate(mode);
        })
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  nodeClicked = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionFrisoNodeClicked),
        tap(({ node }) => {
          this.dialog.open(FrisoNodeDialogComponent, {
            data: node,
            autoFocus: false,
            maxWidth: 600,
          });
        })
      );
    },
    {
      dispatch: false,
    }
  );

  private navigate(mode: string): Observable<boolean> {
    const queryParams: Params = {
      mode,
    };
    const promise = this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
    });
    return from(promise);
  }
}
