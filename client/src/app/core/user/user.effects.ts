import { Location } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { concatLatestFrom } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import * as Sentry from '@sentry/angular-ivy';
import { from } from 'rxjs';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { BrowserStorageService } from '@app/services/browser-storage.service';
import { selectUrl } from '@app/core.state';
import { ReturnUrl } from './return-url';
import { actionUserLoginCompleted } from './user.actions';
import { actionUserLogoutCompleted } from './user.actions';
import { actionUserLogoutReturnUrlRegistered } from './user.actions';
import { actionUserLogoutLinkClicked } from './user.actions';
import { actionUserLoggedOut } from './user.actions';
import { actionUserAuthenticated } from './user.actions';
import { actionUserLoginReturnUrlRegistered } from './user.actions';
import { actionUserLoginLinkClicked } from './user.actions';
import { actionUserLogout } from './user.actions';
import { actionUserLogin } from './user.actions';
import { actionUserReceived } from './user.actions';
import { actionUserInit } from './user.actions';
import { selectUserReturnUrl } from './user.selectors';

@Injectable()
export class UserEffects {
  constructor(
    private actions$: Actions,
    private router: Router,
    private location: Location,
    private browserStorageService: BrowserStorageService,
    private http: HttpClient,
    private store: Store
  ) {
    this.initUser();
  }

  // noinspection JSUnusedGlobalSymbols
  loginLinkClicked = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionUserLoginLinkClicked),
      concatLatestFrom(() => [this.store.select(selectUrl)]),
      map(([_, url]) => {
        let returnUrl = url;
        if (returnUrl.endsWith('/login')) {
          returnUrl = '/';
        }
        return actionUserLoginReturnUrlRegistered({
          returnUrl,
        });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  loginCallbackPageRegistered = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionUserLoginReturnUrlRegistered),
        tap(() => {
          return this.router.navigate(['/login']);
        })
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  login = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionUserLogin),
        concatLatestFrom(() => [this.store.select(selectUserReturnUrl)]),
        mergeMap(([_, returnUrl]) => {
          const encodedReturnUrl = ReturnUrl.encode(returnUrl);
          const baseHref = this.location.prepareExternalUrl('/');
          let loginUrl = '/api/login?callbackUrl=';
          loginUrl += window.location.origin;
          loginUrl += baseHref;
          loginUrl += 'authenticate?page=';
          loginUrl += encodedReturnUrl;
          return this.http.get(loginUrl, {
            responseType: 'text',
          });
        }),
        tap((response) => {
          window.location.href =
            'https://www.openstreetmap.org/oauth/authorize?oauth_token=' +
            response;
        })
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  authenticated = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionUserAuthenticated),
      mergeMap(() => {
        const search = decodeURIComponent(window.location.search);
        return this.http
          .get('/api/authenticated' + search, {
            responseType: 'text',
          })
          .pipe(
            map((user) => {
              this.updateUser(user);
              const returnUrl = ReturnUrl.fromUrl(search);
              return actionUserReceived({ user, returnUrl });
            })
          );
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  authenticatedUserReceived = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionUserReceived),
      concatLatestFrom(() => [this.store.select(selectUserReturnUrl)]),
      mergeMap(([_, returnUrl]) => {
        const promise = this.router.navigateByUrl(returnUrl);
        return from(promise);
      }),
      map(() => actionUserLoginCompleted())
    );
  });

  // noinspection JSUnusedGlobalSymbols
  logoutLinkClicked = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionUserLogoutLinkClicked),
      concatLatestFrom(() => [this.store.select(selectUrl)]),
      map(([_, url]) => {
        let returnUrl = url;
        if (returnUrl.endsWith('/login')) {
          returnUrl = '/';
        }
        return actionUserLogoutReturnUrlRegistered({
          returnUrl,
        });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  logoutReturnUrlRegistered = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionUserLogoutReturnUrlRegistered),
        tap(() => {
          return this.router.navigate(['/logout']);
        })
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  logout = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionUserLogout),
      mergeMap(() => {
        return this.http
          .get('/api/logout', {
            responseType: 'text',
          })
          .pipe(
            map(() => {
              this.updateUser(null);
              return actionUserLoggedOut();
            })
          );
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  loggedOut = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionUserLoggedOut),
      concatLatestFrom(() => [this.store.select(selectUserReturnUrl)]),
      mergeMap(([_, returnUrl]) => {
        const promise = this.router.navigateByUrl(returnUrl);
        return from(promise);
      }),
      map(() => actionUserLogoutCompleted())
    );
  });

  private initUser() {
    const user = this.browserStorageService.get('user');
    this.updateSentryUser(user);
    this.store.dispatch(actionUserInit({ user }));
  }

  private updateUser(user: string | null): void {
    this.updateSentryUser(user);
    if (!!user) {
      this.browserStorageService.set('user', user);
      Sentry.setUser({
        _id: user,
      });
    } else {
      this.browserStorageService.remove('user');
      Sentry.setUser(null);
    }
  }

  private updateSentryUser(user: string | null): void {
    if (!!user) {
      Sentry.setUser({
        _id: user,
      });
    } else {
      Sentry.setUser(null);
    }
  }
}
