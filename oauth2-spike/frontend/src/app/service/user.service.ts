import { HttpClient } from "@angular/common/http";
import { signal } from "@angular/core";
import { Injectable } from "@angular/core";
import { OAuthErrorEvent } from "angular-oauth2-oidc";
import { OAuthService } from "angular-oauth2-oidc";
import { mergeMap } from "rxjs";
import { from } from "rxjs";
import { map } from "rxjs";

@Injectable()
export class UserService {

  private readonly _user = signal<string | null>(null);
  readonly user = this._user.asReadonly();
  private readonly _accessToken = signal<string | null>(null);
  readonly accessToken = this._accessToken.asReadonly();

  constructor(private http: HttpClient, private oauthService: OAuthService) {
    oauthService.events.subscribe(event => {
      if (event instanceof OAuthErrorEvent) {
        console.error('UserService OAuthErrorEvent:', event);
      } else {
        console.info('UserService OAuthEvent:', event);
      }
    });

    this.http.get('/api/client-id', {responseType: 'text'}).pipe(
      mergeMap((clientId) => {
        console.log(`UserService.login() received clientId: ${clientId}`);
        const authConfig = {
          issuer: 'https://www.openstreetmap.org',
          strictDiscoveryDocumentValidation: false, // TODO can remove?
          redirectUri: 'http://127.0.0.1:4200', // TODO make dynamic (include redirectUrl)
          clientId: clientId,
          responseType: 'code',
          scope: 'read_prefs',
          showDebugInformation: true,
          timeoutFactor: 0.25,
          // timeoutFactor: 0.01,
          oidc: false, // added to avoid scope 'openid' to be added automatically in authorize request
        };
        console.log(`UserService.authLogin() configure()`);
        this.oauthService.configure(authConfig);
        console.log(`UserService.constructor accessToken=` + oauthService.getAccessToken());
        this._accessToken.set(oauthService.getAccessToken());

        const obs = from(this.oauthService.loadDiscoveryDocumentAndTryLogin())
        return obs.pipe(
          map((value) =>  {
            console.log('aa000 value=' + value);
            const xx = this.oauthService.getAccessToken();
            console.log('aaaaa xx=' + xx);
            this._accessToken.set(xx);
            console.log('bbbbb xx=' + xx);
            return value;
          })
        );
      })
    ).subscribe();
  }

  updateAccessToken(): void {
    console.log(`accessToken=${this.oauthService.getAccessToken()}`);
    this._accessToken.set(this.oauthService.getAccessToken());
  }

  refresh() {
    this.oauthService.refreshToken();
  }

  login(): void {
    console.log(`UserService.authLogin() loadDiscoveryDocumentAndLogin()`);
    const obs = from(this.oauthService.loadDiscoveryDocumentAndLogin())
    obs.subscribe((value) => {
      console.log('aa000 value=' + value);
      const xx = this.oauthService.getAccessToken();
      console.log('aaaaa xx=' + xx);
      this._accessToken.set(xx);
      console.log('bbbbb xx=' + xx);
      return value;
    });
  }
}
