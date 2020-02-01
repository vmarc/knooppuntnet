import {Component} from "@angular/core";
import {UserService} from "../../../services/user.service";
import {Router} from "@angular/router";

/* tslint:disable:template-i18n */
@Component({
  selector: "kpn-sidebar-footer",
  template: `
    <div class="footer">
      <p>
        <a [href]="link('en')" class="link-list-entry">English</a>
        <a [href]="link('nl')" class="link-list-entry">Nederlands</a>
        <a [href]="link('fr')" class="link-list-entry">Français</a>
        <a [href]="link('de')">Deutsch</a>
      </p>

      <p class="version">
        v3.0.0-alpha-6
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
    return this.userService.isLoggedIn();
  }

  link(language: string): string {
    let path = this.router.url;
    if (path.startsWith("/en/") || path.startsWith("/nl/") || path.startsWith("/fr/") || path.startsWith("/de/")) {
      path = path.substring(3);
    }
    return `/${language}${path}`;
  }

}
