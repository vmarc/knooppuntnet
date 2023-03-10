import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Params } from '@angular/router';
import { concatLatestFrom } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { actionFrisoMode } from './friso.actions';
import { actionFrisoPageInit } from './friso.actions';
import { actionFrisoPageLoad } from './friso.actions';
import { actionFrisoPageLoaded } from './friso.actions';
import { selectQueryParam } from '@app/core/core.state';
import { from } from 'rxjs';

@Injectable()
export class FrisoEffects {
  // noinspection JSUnusedGlobalSymbols
  frisoPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionFrisoPageInit),
      concatLatestFrom(() => this.store.select(selectQueryParam('mode'))),
      map(([_, modeParam]) => {
        let mode = 'rename';
        if (!!modeParam) {
          mode = modeParam;
        }
        return actionFrisoPageLoad({ mode });
      })
    );
  });

  frisoMode = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionFrisoPageLoad, actionFrisoMode),
      mergeMap(({ mode }) => {
        const queryParams: Params = {
          mode,
        };
        const promise = this.router.navigate([], {
          relativeTo: this.route,
          queryParams,
        });
        return from(promise).pipe(map((response) => actionFrisoPageLoaded()));
      })
    );
  });

  constructor(
    private actions$: Actions,
    private store: Store,
    private router: Router,
    private route: ActivatedRoute
  ) {}
}
