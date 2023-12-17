import { DOCUMENT } from '@angular/common';
import { HttpParams } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { OnDestroy } from '@angular/core';
import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { OAuthSuccessEvent } from 'angular-oauth2-oidc';
import { OAuthService } from 'angular-oauth2-oidc';
import { map } from 'rxjs';
import { Observable } from 'rxjs';
import { tap } from 'rxjs';
import { BrowserStorageService } from './browser-storage.service';
import { Subscriptions } from './subscriptions';
import { UserStore } from './user.store';

@Injectable()
export class UserService implements OnDestroy {
  private readonly userStore = inject(UserStore);

  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly oauthService = inject(OAuthService);
  private readonly browserStorageService = inject(BrowserStorageService);
  private readonly document = inject(DOCUMENT);

  private readonly subscriptions = new Subscriptions();
  private readonly localStorageUserKey = 'osm-user-name'; // using new key to avoid clash with old login mechanism
  private readonly oldLocalStorageUserKey = 'user';
  private showDebugInformation = true;
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
    this.loadDiscoveryDocumentAndTryLogin()
      .pipe(
        tap(() => {
          if (this.returnUrl) {
            this.debug(`login() initCodeFlow() returnUrl=${this.returnUrl}`);
            this.oauthService.initCodeFlow(this.returnUrl);
          }
        })
      )
      .subscribe({
        next: (v) => this.debug('login() next', v),
        error: (e) => this.debug('login() next', e),
        complete: () => this.debug('login() next completed.'),
      });
  }

  authenticated(): void {
    this.loadDiscoveryDocumentAndTryLogin().subscribe({
      next: (v) => this.debug('authenticated() next', v),
      error: (e) => this.debug('authenticated() error', e),
      complete: () => this.debug('authenticated() completed.'),
    });
  }

  logout(): void {
    this.http
      .get('/api/logout', { responseType: 'text' })
      .pipe(
        tap(() => {
          this.updateUser(null);
          if (this.returnUrl) {
            const url = this.returnUrl;
            this.returnUrl = null;
            this.debug(`navigateByUrl(${url})`);
            this.router.navigateByUrl(url);
          }
        })
      )
      .subscribe({
        next: (v) => this.debug('logout() next', v),
        error: (e) => this.debug('logout() error', e),
        complete: () => this.debug('logout() completed.'),
      });
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
        this.debug('OAuthEvent:', event);
        if (event instanceof OAuthSuccessEvent) {
          const successEvent = event as OAuthSuccessEvent;
          if (successEvent.type === 'token_received') {
            this.tokenReceived();
          }
        }
      })
    );
  }

  private initUser() {
    // TODO remove following line after one month or so (only needed short time to clean up old mechanism)
    this.browserStorageService.remove(this.oldLocalStorageUserKey);
    const userFromStorage = this.browserStorageService.get(this.localStorageUserKey);
    // this.updateSentryUser(userFromStorage);
    this.userStore.updateUser(userFromStorage);
    this.debug(`user from storage: ${userFromStorage}`);
  }

  private loadDiscoveryDocumentAndTryLogin(): Observable<void> {
    return this.http.get('/api/client-id', { responseType: 'text' }).pipe(
      map((clientId) => {
        this.debug(`configure clientId: ${clientId}`);
        const authConfigWithClientId = {
          ...this.authConfig,
          clientId: clientId,
        };
        this.oauthService.configure(authConfigWithClientId);
        this.oauthService.loadDiscoveryDocumentAndTryLogin();
      })
    );
  }

  private updateUser(user: string | null): void {
    // this.updateSentryUser(user);
    if (user) {
      this.browserStorageService.set(this.localStorageUserKey, user);
      // Sentry.setUser({
      //   _id: user,
      // });
    } else {
      this.browserStorageService.remove(this.localStorageUserKey);
      // Sentry.setUser(null);
    }
    this.userStore.updateUser(user);
  }

  private tokenReceived(): void {
    const accessToken = this.oauthService.getAccessToken();
    this.debug(`received accessToken: ${accessToken}`);
    if (accessToken) {
      const params = new HttpParams().set('accessToken', accessToken);
      this.http
        .get('/api/authenticated', { params: params, responseType: 'text' })
        .pipe(
          tap((username) => {
            this.updateUser(username);
            this.oauthService.logOut();
            // TODO handle UNAUTHORIZED
            if (this.oauthService.state) {
              const url = decodeURIComponent(this.oauthService.state);
              this.router.navigateByUrl(url);
            }
          })
        )
        .subscribe({
          next: (v) => this.debug('/api/authenticated HTTP response', v),
          error: (e) => this.debug('/api/authenticated HTTP Error', e),
          complete: () => this.debug('/api/authenticated HTTP request completed.'),
        });
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
