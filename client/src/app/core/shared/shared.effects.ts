import { LocationStrategy } from '@angular/common';
import { Location } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
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
import { EditDialogComponent } from '../../analysis/components/edit/edit-dialog.component';
import { BrowserStorageService } from '../../services/browser-storage.service';
import { selectUrl } from '../core.state';
import { AppState } from '../core.state';
import { actionSharedUserReceived } from './shared.actions';
import { actionSharedLoginCallbackPageRegistered } from './shared.actions';
import { actionSharedRegisterLoginCallbackPage } from './shared.actions';
import { actionSharedAuthenticated } from './shared.actions';
import { actionSharedLogin } from './shared.actions';
import { actionSharedLogout } from './shared.actions';
import { actionSharedInitUser } from './shared.actions';
import { actionSharedUser } from './shared.actions';
import { actionSharedLanguage } from './shared.actions';
import { actionSharedEdit } from './shared.actions';
import { selectSharedLoginCallbackPage } from './shared.selectors';
import { selectSharedLanguage } from './shared.selectors';

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
    this.initUser();
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

  // noinspection JSUnusedGlobalSymbols
  setSentryUser = createEffect(
    () =>
      this.actions$.pipe(
        ofType(
          actionSharedInitUser,
          actionSharedUser,
          actionSharedUserReceived
        ),
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
        ofType(actionSharedUser, actionSharedUserReceived),
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
        ofType(actionSharedLogin),
        concatLatestFrom(() => [
          this.store.select(selectSharedLanguage),
          this.store.select(selectSharedLoginCallbackPage),
        ]),

        mergeMap(([{}, language, loginCallbackPage]) => {
          let loginUrl = '/api/login?callbackUrl=';
          loginUrl += window.location.origin;
          if (!!language) {
            loginUrl += '/' + language;
          }
          loginUrl += '/authenticate?page=';
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
      ofType(actionSharedLogout),
      map(() =>
        this.http.get('/api/logout', {
          responseType: 'text',
        })
      ),
      map((response) => {
        return actionSharedUser({ user: null });
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  registerLoginCallbackPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSharedRegisterLoginCallbackPage),
      concatLatestFrom(() => [
        this.store.select(selectSharedLanguage),
        this.store.select(selectUrl),
      ]),
      map(([{}, language, url]) => {
        let loginCallbackPage = '';
        if (url.endsWith('/login')) {
          if (!!language) {
            loginCallbackPage += `/${language}/`;
          } else {
            loginCallbackPage += '/';
          }
        } else {
          loginCallbackPage += url;
        }
        return actionSharedLoginCallbackPageRegistered({ loginCallbackPage });
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  loginCallbackPageRegistered = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionSharedLoginCallbackPageRegistered),
        concatLatestFrom(() => [this.store.select(selectSharedLanguage)]),
        tap(([{}, language]) => {
          let url = '/login';
          if (!!language) {
            url = '/' + language + url;
          }
          return this.router.navigate([url]);
        })
      ),
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  authenticated = createEffect(() =>
    this.actions$.pipe(
      ofType(actionSharedAuthenticated),
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
              return actionSharedUserReceived({ user, pageArray });
            })
          );
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  authenticatedUserReceived = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionSharedUserReceived),
        tap((action) => {
          this.router.navigate(action.pageArray);
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

  private initUser() {
    const user = this.browserStorageService.get('user');
    this.store.dispatch(actionSharedUser({ user }));
  }
}
