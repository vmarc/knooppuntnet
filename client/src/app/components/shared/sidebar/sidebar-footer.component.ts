import {Component} from "@angular/core";
import {UserService} from "../../../services/user.service";
import {Router} from "@angular/router";

@Component({
  selector: "kpn-sidebar-footer",
  template: `
    <kpn-sidebar-version-warning></kpn-sidebar-version-warning>
    <div class="footer">
      <p>
        <a [href]="link('en')">English</a>
        |
        <a [href]="link('nl')">Nederlands</a>
        |
        <a [href]="link('fr')">Français</a>
        |
        <a [href]="link('de')">Deutsch</a>
      </p>

      <p class="version">
        2019-06-16
      </p>

      <p *ngIf="isLoggedIn()">
        {{currentUser()}}
        <br>
        <kpn-link-logout></kpn-link-logout>
      </p>

      <p *ngIf="!isLoggedIn()">
        <kpn-link-login></kpn-link-login>
      </p>

    </div>
  `,
  styles: [`
    .footer {
      border-top-width: 1px;
      border-top-style: solid;
      border-top-color: lightgray;
      text-align: center;
    }

    .version {
      color: lightgray;
    }
  `]
})
export class SidebarFooterComponent {

  constructor(private router: Router,
              private userService: UserService) {
  }

  currentUser(): string {
    return this.userService.currentUser();
  }

  isLoggedIn(): boolean {
    return this.currentUser().length > 0;
  }

  link(language: string): string {
    let path = this.router.url;
    if (path.startsWith("/en/") || path.startsWith("/nl/") || path.startsWith("/fr/") || path.startsWith("/de/")) {
      path = path.substring(3);
    }
    return `/${language}${path}`;
  }

}
