import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from "@angular/router";
import { RouterOutlet } from '@angular/router';
import { OAuthErrorEvent } from "angular-oauth2-oidc";
import { OAuthService } from "angular-oauth2-oidc";
import { UserService } from "./service/user.service";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink],
  template: `
    <p>oauth2 spike</p>
    <p class="buttons">
      <a routerLink="/">home</a>
      <a routerLink="/page1">page1</a>
      <a routerLink="/page2">page2</a>
      <a routerLink="/page3">page3</a>
    </p>
    <p class="buttons">
      <button (click)="login()">login</button>
      <button (click)="logout()">logout</button>
      <button (click)="updateAccessToken()">update accessToken</button>
    </p>
    <hr />
    <router-outlet></router-outlet>
    <hr />
    <p>User: {{user()}}</p>
    <p>Access token: {{accessToken()}}</p>
  `,
  styles: `
    .buttons {
      display: flex;
      gap: 1em;
    }
  `,
  providers: [
    {provide: UserService}
  ]
})
export class AppComponent {

  user = this.userService.user;
  accessToken = this.userService.accessToken;

  constructor(private userService: UserService) {
  }

  login(): void {
    this.userService.login();
  }

  updateAccessToken(): void {
    this.userService.updateAccessToken();
  }

  logout(): void {
  }
}
