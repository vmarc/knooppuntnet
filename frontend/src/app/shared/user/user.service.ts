import { DOCUMENT } from '@angular/common';
import { HttpContext } from '@angular/common/http';
import { HttpErrorResponse } from '@angular/common/http';
import { HttpParams } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { OnDestroy } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BrowserStorageService } from '@app/services';
import { LOCAL_ERROR_HANDLING } from '@app/spinner';
import { Subscriptions } from '@app/util';
import { OAuthErrorEvent } from 'angular-oauth2-oidc';
import { OAuthInfoEvent } from 'angular-oauth2-oidc';
import { OAuthSuccessEvent } from 'angular-oauth2-oidc';
import { OAuthService } from 'angular-oauth2-oidc';
import { filter } from 'rxjs';
import { of } from 'rxjs';
import { catchError } from 'rxjs';
import { map } from 'rxjs';
import { Observable } from 'rxjs';
import { tap } from 'rxjs';
import { UserStore } from './user.store';
import * as Sentry from '@sentry/angular-ivy';

interface AuthorizationError {
  error: string;
  error_description: string;
}

@Injectable()
export class UserService implements OnDestroy {
  private readonly userStore = inject(UserStore);

  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly oauthService = inject(OAuthService);
  private readonly browserStorageService = inject(BrowserStorageService);
  private readonly document = inject(DOCUMENT);

  private readonly subscriptions = new Subscriptions();
  private readonly localStorageUserKey = 'osm-user'; // using new key to avoid clash with old login mechanism
  private readonly oldLocalStorageUserKey = 'user';
  private showDebugInformation = false;
  private returnUrl: string | null = null;

  private authConfig = {
    issuer: 'https://www.openstreetmap.org',
    redirectUri: this.composeRedirectUri(),
    responseType: 'code',
    scope: 'read_prefs',
    showDebugInformation: this.showDebugInformation,
    timeoutFactor: 0.01,
    oidc: false, // added to avoid scope 'openid' to be added automatically in authorize request
  };

  private readonly locationErrorHandlingContext = new HttpContext().set(LOCAL_ERROR_HANDLING, true);

  constructor() {
    this.initEventHandling();
    this.initUser();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  navigateToReturnUrl(): void {
    if (this.returnUrl) {
      const url = this.returnUrl;
      this.returnUrl = null;
      this.router.navigateByUrl(url);
    }
  }

  login(): void {
    this.userStore.resetError();
    this.loadDiscoveryDocumentAndTryLogin().subscribe(() => {
      if (this.returnUrl) {
        this.debug(`login() initCodeFlow() returnUrl=${this.returnUrl}`);
        this.oauthService.initCodeFlow(this.returnUrl);
      }
    });
  }

  authenticated(): void {
    this.loadDiscoveryDocumentAndTryLogin().subscribe();
  }

  logout(): void {
    const context = this.locationErrorHandlingContext;
    this.http
      .get('/api/logout', { context, responseType: 'text' }) // erase the cookie
      .subscribe({
        next: () => this.logoutUser(),
        error: (error) => this.logoutError(error),
      });
  }

  private logoutUser(): void {
    this.updateUser(null);
    if (this.returnUrl) {
      const url = this.returnUrl;
      this.returnUrl = null;
      this.debug(`navigateByUrl(${url})`);
      this.router.navigateByUrl(url);
    }
  }

  private logoutError(error: any): void {
    if (error instanceof HttpErrorResponse) {
      const httpErrorResponse = error as HttpErrorResponse;
      this.userStore.updateError('Could not logout');
      this.userStore.updateErrorDetail(httpErrorResponse.message);
    }
    this.debug('logout() error', error);
  }

  loginLinkClicked(): void {
    this.returnUrl = this.router.url; // preserve url to return to after login
    this.router.navigate(['/login']);
  }

  logoutLinkClicked(): void {
    this.returnUrl = this.router.url; // preserve url to return to after logout
    this.router.navigate(['/logout']);
  }

  private initEventHandling(): void {
    this.subscriptions.add(
      this.oauthService.events.subscribe((event) => {
        if (event instanceof OAuthSuccessEvent) {
          const successEvent = event as OAuthSuccessEvent;
          this.handleSuccessEvent(successEvent);
        } else if (event instanceof OAuthInfoEvent) {
          const infoEvent = event as OAuthInfoEvent;
          this.handleInfoEvent(infoEvent);
        } else if (event instanceof OAuthErrorEvent) {
          const errorEvent = event as OAuthErrorEvent;
          this.handleErrorEvent(errorEvent);
        } else {
          this.debug('OAuthEvent:', event);
        }
      })
    );
  }

  private handleSuccessEvent(successEvent: OAuthSuccessEvent): void {
    if (successEvent.type === 'token_received') {
      this.tokenReceived();
    } else {
      this.debug('OAuthSuccessEvent:', successEvent);
    }
  }

  private handleInfoEvent(infoEvent: OAuthInfoEvent): void {
    this.debug('OAuthInfoEvent:', infoEvent.info);
  }

  private handleErrorEvent(errorEvent: OAuthErrorEvent): void {
    this.debug('OAuthErrorEvent:', errorEvent);
    if (errorEvent.type === 'discovery_document_load_error') {
      const message = $localize`:@@user.login.discovery-document-load-error:Login failed while trying to retrieve security configuration from OpenStreetMap.`;
      this.userStore.updateError(message);
      if (errorEvent.reason instanceof HttpErrorResponse) {
        const httpErrorResponse = errorEvent.reason as HttpErrorResponse;
        this.userStore.updateErrorDetail(httpErrorResponse.message);
      }
    } else if (errorEvent.type === 'code_error') {
      if (errorEvent.params) {
        const authorizationError = errorEvent.params as AuthorizationError;
        if (authorizationError.error && authorizationError.error === 'access_denied') {
          const message = $localize`:@@user.login.access-denied:Login failed, received access denied from OpenStreetMap.`;
          this.userStore.updateError(message);
          this.userStore.updateErrorDetail(authorizationError.error_description);
        } else {
          const message = $localize`:@@user.login.login-failed:Login failed`;
          this.userStore.updateError(message + ', ' + authorizationError.error);
          this.userStore.updateErrorDetail(JSON.stringify(errorEvent.params));
        }
      }
    } else {
      this.userStore.updateError('Login failed: ' + errorEvent.type);
      if (errorEvent.reason instanceof HttpErrorResponse) {
        const httpErrorResponse = errorEvent.reason as HttpErrorResponse;
        this.userStore.updateErrorDetail(httpErrorResponse.message);
      }
    }
  }

  private initUser() {
    // TODO remove following line after one month or so (only needed short time to clean up old mechanism)
    this.browserStorageService.remove(this.oldLocalStorageUserKey);
    const userFromStorage = this.browserStorageService.get(this.localStorageUserKey);
    this.updateSentryUser(userFromStorage);
    this.userStore.updateUser(userFromStorage);
    this.debug(`user from storage: ${userFromStorage}`);
  }

  private loadDiscoveryDocumentAndTryLogin(): Observable<void> {
    return this.loadClientId().pipe(
      filter((clientId) => clientId !== null),
      map((clientId) => {
        this.debug(`configure clientId: ${clientId}`);
        const authConfigWithClientId = {
          ...this.authConfig,
          clientId: '' + clientId,
        };
        this.oauthService.configure(authConfigWithClientId);
        this.oauthService.loadDiscoveryDocumentAndTryLogin();
      })
    );
  }

  private loadClientId(): Observable<string | null> {
    const context = this.locationErrorHandlingContext;
    return this.http.get('/api/client-id', { context, responseType: 'text' }).pipe(
      catchError((error) => {
        console.log([`loadCLientId() error`, error]);
        if (error instanceof HttpErrorResponse) {
          const httpErrorResponse = error as HttpErrorResponse;
          const message = $localize`:@@user.login.client-id-error:Could not get clientId from knooppuntnet server.`;
          this.userStore.updateError(message);
          this.userStore.updateErrorDetail(httpErrorResponse.message);
        }
        return of(null);
      })
    );
  }

  private updateUser(user: string | null): void {
    this.updateSentryUser(user);
    if (user) {
      this.browserStorageService.set(this.localStorageUserKey, user);
      Sentry.setUser({
        _id: user,
      });
    } else {
      this.browserStorageService.remove(this.localStorageUserKey);
      Sentry.setUser(null);
    }
    this.userStore.updateUser(user);
  }

  private updateSentryUser(user: string | null): void {
    if (user) {
      Sentry.setUser({
        _id: user,
      });
    } else {
      Sentry.setUser(null);
    }
  }

  private tokenReceived(): void {
    const accessToken = this.oauthService.getAccessToken();
    this.debug(`received accessToken: ${accessToken}`);
    if (accessToken) {
      const params = new HttpParams().set('accessToken', accessToken);
      const context = this.locationErrorHandlingContext;
      this.http
        .get('/api/authenticated', { context, params, responseType: 'text' })
        .pipe(
          catchError((error) => {
            if (error instanceof HttpErrorResponse) {
              const httpErrorResponse = error as HttpErrorResponse;
              const message = $localize`:@@user.login.username-failed:Could not get username from knooppuntnet server.`;
              this.userStore.updateError(message);
              this.userStore.updateErrorDetail(httpErrorResponse.message);
            }
            return of(null);
          }),
          tap((username) => {
            this.updateUser(username);
            this.oauthService.logOut();
            if (this.oauthService.state) {
              const url = decodeURIComponent(this.oauthService.state);
              this.router.navigateByUrl(url);
            }
          })
        )
        .subscribe();
    }
  }

  private composeRedirectUri() {
    if (this.document.location.hostname === 'localhost') {
      return `http://127.0.0.1:4000/authenticated`;
    }
    const origin = this.document.location.origin;
    const path = this.document.location.pathname;
    let language = '';
    if (path.length >= 4) {
      if (
        path.startsWith('/en/') ||
        path.startsWith('/nl/') ||
        path.startsWith('/fr/') ||
        path.startsWith('/de/')
      ) {
        language = path.substring(0, 3);
      }
    }
    return `${origin}${language}/authenticated`;
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  private debug(message: string, data?: any): void {
    if (this.showDebugInformation) {
      console.log('UserService: ' + message, data);
    }
  }
}
