import { HttpParams } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { OAuthSuccessEvent } from 'angular-oauth2-oidc';
import { OAuthService } from 'angular-oauth2-oidc';
import { tap } from 'rxjs';
import { BrowserStorageService } from './browser-storage.service';

@Injectable()
export class UserService {
  private showDebugInformation = true;
  private returnUrl: string | null = null;

  private readonly _user = signal<string | null>(null);
  readonly user = this._user.asReadonly();

  private authConfig = {
    issuer: 'https://www.openstreetmap.org',
    redirectUri: 'http://127.0.0.1:4200/authenticated', // TODO make dynamic (include redirectUrl)
    responseType: 'code',
    scope: 'read_prefs',
    showDebugInformation: this.showDebugInformation,
    timeoutFactor: 0.01,
    oidc: false, // added to avoid scope 'openid' to be added automatically in authorize request
  };

  constructor(
    private http: HttpClient,
    private router: Router,
    private oauthService: OAuthService,
    private browserStorageService: BrowserStorageService,
  ) {
    this.initEventHandling();
    this.initUser();
  }

  cancelLogin(): void {
    if (this.returnUrl) {
      const url = this.returnUrl;
      this.returnUrl = null;
      this.router.navigateByUrl(url);
    }
  }

  cancelLogout(): void {
    // TODO same as cancelLogin()
    if (this.returnUrl) {
      const url = this.returnUrl;
      this.returnUrl = null;
      this.router.navigateByUrl(url);
    }
  }

  login(): void {
    this.http
      .get('/api/client-id', { responseType: 'text' })
      .pipe(
        tap((clientId) => {
          console.log(`clientId=${clientId}`);
          const authConfigWithClientId = {
            ...this.authConfig,
            clientId: clientId,
          };
          this.oauthService.configure(authConfigWithClientId);
          this.oauthService.loadDiscoveryDocumentAndTryLogin();
          if (this.returnUrl) {
            console.log(`login() initCodeFlow() returnUrl=${this.returnUrl}`);
            this.oauthService.initCodeFlow(this.returnUrl);
          }
        }),
      )
      .subscribe();
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
            this.router.navigateByUrl(url);
          }
        }),
      )
      .subscribe();
  }

  authenticated(): void {
    this.http
      .get('/api/client-id', { responseType: 'text' })
      .pipe(
        tap((clientId) => {
          console.log(`clientId=${clientId}`);
          const authConfigWithClientId = {
            ...this.authConfig,
            clientId: clientId,
          };
          this.oauthService.configure(authConfigWithClientId);
          this.oauthService.loadDiscoveryDocumentAndTryLogin();
        }),
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
          }),
        )
        .subscribe();
    }
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  private debug(message: string, data?: any): void {
    if (this.showDebugInformation) {
      console.log(message, data);
    }
  }
}
