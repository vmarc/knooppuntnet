import { DOCUMENT } from '@angular/common';
import { HttpParams } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { OAuthSuccessEvent } from 'angular-oauth2-oidc';
import { OAuthService } from 'angular-oauth2-oidc';
import { map } from "rxjs";
import { Observable } from "rxjs";
import { tap } from 'rxjs';
import { BrowserStorageService } from './browser-storage.service';

@Injectable()
export class UserService {
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly oauthService = inject(OAuthService);
  private readonly browserStorageService = inject(BrowserStorageService);
  private readonly document = inject(DOCUMENT);

  private showDebugInformation = true;
  private returnUrl: string | null = null;

  private readonly _user = signal<string | null>(null);
  readonly user = this._user.asReadonly();

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

  navigateToReturnUrl(): void {
    if (this.returnUrl) {
      const url = this.returnUrl;
      this.returnUrl = null;
      this.router.navigateByUrl(url);
    }
  }

  login(): void {
    this.loadDiscoveryDocumentAndTryLogin().pipe(
      tap(() => {
        if (this.returnUrl) {
          this.debug(`login() initCodeFlow() returnUrl=${this.returnUrl}`);
          this.oauthService.initCodeFlow(this.returnUrl);
        }
      })
    ).subscribe();
  }

  authenticated(): void {
    this.loadDiscoveryDocumentAndTryLogin().subscribe();
  }

  logout(): void {
    this.http
    .get('/api/logout', {responseType: 'text'})
    .pipe(
      tap(() => {
        this.updateUser(null);
        if (this.returnUrl) {
          const url = this.returnUrl;
          this.returnUrl = null;
          this.router.navigateByUrl(url);
        }
      })
    )
    .subscribe();
  }

  loginLinkClicked(): void {
    this.returnUrl = this.router.url;
    this.router.navigate(['/login']);
  }

  logoutLinkClicked(): void {
    this.returnUrl = this.router.url;
    this.router.navigate(['/logout']);
  }

  private initEventHandling(): void {
    this.oauthService.events.subscribe((event) => {
      this.debug('UserService OAuthEvent:', event);
      if (event instanceof OAuthSuccessEvent) {
        const successEvent = event as OAuthSuccessEvent;
        if (successEvent.type === 'token_received') {
          this.tokenReceived();
        }
      }
    });
  }

  private initUser() {
    const userFromStorage = this.browserStorageService.get('user');
    // this.updateSentryUser(user);
    this._user.set(userFromStorage);
  }

  private loadDiscoveryDocumentAndTryLogin(): Observable<void> {
    return this.http
    .get('/api/client-id', {responseType: 'text'})
    .pipe(
      map((clientId) => {
        console.log(`clientId=${clientId}`);
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
      this.browserStorageService.set('user', user);
      // Sentry.setUser({
      //   _id: user,
      // });
    } else {
      this.browserStorageService.remove('user');
      // Sentry.setUser(null);
    }
    this._user.set(user);
  }

  private tokenReceived(): void {
    const accessToken = this.oauthService.getAccessToken();
    if (accessToken) {
      const params = new HttpParams().set('accessToken', accessToken);
      this.http
      .get('/api/authenticated', {params: params, responseType: 'text'})
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
      console.log(message, data);
    }
  }
}
