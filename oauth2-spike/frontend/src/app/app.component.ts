import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { OAuthErrorEvent } from "angular-oauth2-oidc";
import { OAuthService } from "angular-oauth2-oidc";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  template: `
    <p>oauth2 spike</p>
    <p class="buttons">
      <a href="/page1">page1</a>
      <a href="/page2">page2</a>
      <a href="/page3">page3</a>
    </p>
    <p class="buttons">
      <button (click)="login()">Login</button>
      <button (click)="refresh()">Refresh</button>
    </p>
    <hr/>
    <router-outlet></router-outlet>
    <hr/>
    <p>User: {{userName}}</p>
    <p>Id token: {{idToken}}</p>
    <p>Access token: {{accessToken}}</p>
  `,
  styles: `
    .buttons {
      display: flex;
      gap: 1em;
    }
  `,
})
export class AppComponent {

  constructor(private oauthService: OAuthService) {
    this.oauthService.events.subscribe(event => {
      if (event instanceof OAuthErrorEvent) {
        console.error('OAuthErrorEvent Object:', event);
      } else {
        console.warn('OAuthEvent Object:', event);
      }
    });
  }

  get userName(): string {
    const claims = this.oauthService.getIdentityClaims();
    if (!claims) return 'unknown';
    return claims['given_name'];
  }

  get idToken(): string {
    return this.oauthService.getIdToken();
  }

  get accessToken(): string {
    return this.oauthService.getAccessToken();
  }

  login(): void {
    const authConfig = {
      issuer: 'https://www.openstreetmap.org',
      strictDiscoveryDocumentValidation: false,
      redirectUri: 'http://127.0.0.1:4200',
      clientId: 'xxx',
      responseType: 'code',
      scope: 'read_prefs',
      showDebugInformation: true,
      timeoutFactor: 0.01,
      oidc: false, // added to avoid scope 'openid' to be added automatically in authorize request
    };
    this.oauthService.configure(authConfig);
    this.oauthService.loadDiscoveryDocumentAndLogin();
  }

  refresh() {
    this.oauthService.refreshToken();
  }

}
