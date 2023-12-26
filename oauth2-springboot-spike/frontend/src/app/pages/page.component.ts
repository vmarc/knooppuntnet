import { inject } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { RouterLink } from '@angular/router';
import { UserStore } from '../user/user.store';
import { UserLinkLoginComponent } from '../user/user-link-login.component';
import { UserLinkLogoutComponent } from '../user/user-link-logout.component';

@Component({
  selector: 'kpn-page',
  standalone: true,
  template: `
    <h1>{{ title }}</h1>
    <p class="menu">
      <a routerLink="/">home</a>
      <a routerLink="/page1">page1</a>
      <a routerLink="/page2">page2</a>
      <a routerLink="/page3">page3</a>
    </p>
    <hr />
    <ng-content />
    <hr />

    @if (user(); as name) {
      <p>Logged in as: {{ name }}</p>
      <kpn-user-link-logout />
    } @else {
      <p>Not logged in</p>
      <kpn-user-link-login />
    }
  `,
  imports: [RouterLink, RouterOutlet, UserLinkLoginComponent, UserLinkLogoutComponent],
})
export class PageComponent {
  private readonly userStore = inject(UserStore);
  readonly user = this.userStore.user;

  @Input({ required: true }) title: string = '';
}
