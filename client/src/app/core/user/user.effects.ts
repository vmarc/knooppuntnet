import { Location } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { concatLatestFrom } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import * as Sentry from '@sentry/angular';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { BrowserStorageService } from '../../services/browser-storage.service';
import { selectUrl } from '../core.state';
import { AppState } from '../core.state';
import { actionUserAuthenticated } from './user.actions';
import { actionUserLoginCallbackPageRegistered } from './user.actions';
import { actionUserLoginLinkClicked } from './user.actions';
import { actionUserLogout } from './user.actions';
import { actionUserLogin } from './user.actions';
import { actionUserSet } from './user.actions';
import { actionUserReceived } from './user.actions';
import { actionUserInit } from './user.actions';
import { selectUserLoginCallbackPage } from './user.selectors';

@Injectable()
export class UserEffects {
  constructor(
    private actions$: Actions,
    private router: Router,
    private location: Location,
    private browserStorageService: BrowserStorageService,
    private http: HttpClient,
    private store: Store<AppState>
  ) {
    this.initUser();
  }

  // noinspection JSUnusedGlobalSymbols
  setSentryUser = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionUserInit, actionUserSet, actionUserReceived),
        tap(({ user }) => {
          if (user !== null) {
            Sentry.setUser({ id: user });
          }
        })
      ),
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  setUser = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionUserSet, actionUserReceived),
        tap(({ user }) => {
          if (!!user) {
            this.browserStorageService.set('user', user);
          } else {
            this.browserStorageService.remove('user');
          }
          Sentry.setUser(null);
        })
      ),
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  login = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionUserLogin),
        concatLatestFrom(() => [
          this.store.select(selectUserLoginCallbackPage),
        ]),
        mergeMap(([{}, loginCallbackPage]) => {
          const baseHref = this.location.prepareExternalUrl('/');
          let loginUrl = '/api/login?callbackUrl=';
          loginUrl += window.location.origin;
          loginUrl += baseHref;
          loginUrl += 'authenticate?page=';
          loginUrl += loginCallbackPage;
          return this.http.get(loginUrl, {
            responseType: 'text',
          });
        }),
        tap((response) => {
          window.location.href =
            'https://www.openstreetmap.org/oauth/authorize?oauth_token=' +
            response;
        })
      ),
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  logout = createEffect(() =>
    this.actions$.pipe(
      ofType(actionUserLogout),
      map(() =>
        this.http.get('/api/logout', {
          responseType: 'text',
        })
      ),
      map(({}) => {
        return actionUserSet({ user: null });
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  loginLinkClicked = createEffect(() =>
    this.actions$.pipe(
      ofType(actionUserLoginLinkClicked),
      concatLatestFrom(() => [this.store.select(selectUrl)]),
      map(([{}, url]) => {
        let loginCallbackPage = url;
        if (loginCallbackPage.endsWith('/login')) {
          loginCallbackPage = '/';
        }
        return actionUserLoginCallbackPageRegistered({ loginCallbackPage });
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  loginCallbackPageRegistered = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionUserLoginCallbackPageRegistered),
        tap(() => {
          return this.router.navigate(['/login']);
        })
      ),
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  authenticated = createEffect(() =>
    this.actions$.pipe(
      ofType(actionUserAuthenticated),
      mergeMap(() => {
        const search = decodeURIComponent(window.location.search);
        return this.http
          .get('/api/authenticated' + search, {
            responseType: 'text',
          })
          .pipe(
            map((user) => {
              const withoutQuestionMark = search.substring(1);
              const firstParam = withoutQuestionMark.split('&')[0];
              const keyAndValue = firstParam.split('=');
              const page = keyAndValue[1].substring(1);
              const pageArray = page.split('/');
              return actionUserReceived({ user, pageArray });
            })
          );
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  authenticatedUserReceived = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionUserReceived),
        tap((action) => {
          this.router.navigate(action.pageArray);
        })
      ),
    { dispatch: false }
  );

  private initUser() {
    const user = this.browserStorageService.get('user');
    this.store.dispatch(actionUserSet({ user }));
  }
}
