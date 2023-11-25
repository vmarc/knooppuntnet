import { HttpParams } from "@angular/common/http";
import { HttpClient } from "@angular/common/http";
import { signal } from "@angular/core";
import { Injectable } from "@angular/core";
import { OAuthInfoEvent } from "angular-oauth2-oidc";
import { OAuthSuccessEvent } from "angular-oauth2-oidc";
import { OAuthErrorEvent } from "angular-oauth2-oidc";
import { OAuthService } from "angular-oauth2-oidc";
import { mergeMap } from "rxjs";
import { tap } from "rxjs";

@Injectable()
export class UserService {

  private showDebugInformation = true;

  private readonly _user = signal<string | null>(null);
  readonly user = this._user.asReadonly();

  constructor(private http: HttpClient, private oauthService: OAuthService) {
    oauthService.events.subscribe(event => {
      if (event instanceof OAuthErrorEvent) {
        console.error('UserService OAuthErrorEvent:', event);
      } else {
        console.info('UserService OAuthEvent:', event);
        if (event instanceof OAuthSuccessEvent) {
          const successEvent = event as OAuthSuccessEvent;
          if (successEvent.type === 'token_received') {
            const accessToken = this.oauthService.getAccessToken();
            if (accessToken) {
              const params = new HttpParams().set('accessToken', accessToken);
              this.http.get('/api/authenticated', {params: params, responseType: 'text'}).pipe(
                tap((username) => {
                  this._user.set(username);
                  // TODO handle UNAUTHORIZED
                })
              ).subscribe();
            }
          }
        } else if (event instanceof OAuthInfoEvent) {
          const infoEvent = event as OAuthSuccessEvent;
          if (infoEvent.type === 'logout') {
            this.debug('resetting user after logout');
            this._user.set(null);
          }
        }
      }
    });

    this.http.get('/api/client-id', {responseType: 'text'}).pipe(
      tap((clientId) => {
        this.debug(`UserService.login() received clientId: ${clientId}`);

        const authConfig = {
          issuer: 'https://www.openstreetmap.org',
          strictDiscoveryDocumentValidation: false, // TODO can remove?
          redirectUri: 'http://127.0.0.1:4200/authenticated', // TODO make dynamic (include redirectUrl)
          clientId: clientId,
          responseType: 'code',
          scope: 'read_prefs',
          showDebugInformation: true,
          timeoutFactor: 0.01,
          oidc: false, // added to avoid scope 'openid' to be added automatically in authorize request
        };
        this.oauthService.configure(authConfig);
        this.oauthService.loadDiscoveryDocumentAndTryLogin();
      })
    );
  }

  login(): void {
    this.oauthService.initCodeFlow();
  }

  logout(): void {
    this.oauthService.logOut();
  }

  authenticated(): void {
  }

  private debug(message: string): void {
    if (this.showDebugInformation){
      console.log(message);
    }
  }

}
