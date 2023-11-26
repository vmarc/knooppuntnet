import { NgIf } from "@angular/common";
import { Input } from "@angular/core";
import { Component } from '@angular/core';
import { RouterOutlet } from "@angular/router";
import { RouterLink } from "@angular/router";
import { UserService } from "../service/user.service";
import { LinkLoginComponent } from "./link-login.component";
import { LinkLogoutComponent } from "./link-logout.component";

@Component({
  selector: 'kpn-page',
  standalone: true,
  template: `
    <h1>{{title}}</h1>
    <p class="menu">
      <a routerLink="/">home</a>
      <a routerLink="/page1">page1</a>
      <a routerLink="/page2">page2</a>
      <a routerLink="/page3">page3</a>
    </p>
    <hr />
    <ng-content />
    <hr />

    <ng-container *ngIf="user() as name; else notLoggedIn" class="menu">
      <p>Logged in as: {{name}}</p>
      <p><kpn-link-logout/></p>
    </ng-container>
    <ng-template #notLoggedIn>
      <p>Not logged in</p>
      <p><kpn-link-login /></p>
    </ng-template>
  `,
  imports: [
    LinkLoginComponent,
    NgIf,
    RouterLink,
    RouterOutlet,
    LinkLogoutComponent
  ]
})
export class PageComponent {
  @Input({ required: true }) title: string = '';

  user = this.userService.user;

  constructor(private userService: UserService) {
  }

  logout(): void {
    this.userService.logout();
  }
}
