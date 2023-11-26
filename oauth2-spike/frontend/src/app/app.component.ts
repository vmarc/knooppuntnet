import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from "@angular/router";
import { RouterOutlet } from '@angular/router';
import { LinkLoginComponent } from "./pages/link-login.component";
import { UserService } from "./service/user.service";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, LinkLoginComponent],
  template: `
    <p>knooppuntnet - oauth2 tryout</p>
    <p class="menu">
      <a routerLink="/">home</a>
      <a routerLink="/page1">page1</a>
      <a routerLink="/page2">page2</a>
      <a routerLink="/page3">page3</a>
    </p>
    <hr />
    <router-outlet></router-outlet>
    <hr />

    <p *ngIf="user() as name; else notLoggedIn" class="menu">
      <span>{{name}}</span>
      <a (click)="logout()">logout</a>
    </p>
    <ng-template #notLoggedIn>
        <kpn-link-login/>
    </ng-template>
  `,
  styles: `
    .menu {
      display: flex;
      gap: 1em;
    }
  `,
})
export class AppComponent {

  user = this.userService.user;

  constructor(private userService: UserService) {
  }

  logout(): void {
    this.userService.logout();
  }
}
