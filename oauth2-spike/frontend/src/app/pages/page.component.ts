import { inject } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { RouterLink } from '@angular/router';
import { UserService } from '../service/user.service';
import { UserStore } from '../service/user.store';
import { LinkLoginComponent } from './link-login.component';
import { LinkLogoutComponent } from './link-logout.component';

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
      <kpn-link-logout />
    } @else {
      <p>Not logged in</p>
      <kpn-link-login />
    }
  `,
  imports: [LinkLoginComponent, RouterLink, RouterOutlet, LinkLogoutComponent],
})
export class PageComponent {
  private readonly userService = inject(UserService);
  private readonly userStore = inject(UserStore);

  @Input({ required: true }) title: string = '';

  readonly user = this.userStore.user;

  logout(): void {
    this.userService.logout();
  }
}
