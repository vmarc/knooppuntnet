import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";
import {Router} from "@angular/router";
import {UserService} from "../../../services/user.service";

/* tslint:disable:template-i18n */
@Component({
  selector: "kpn-sidebar-footer",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="footer">
      <ul class="links">
        <li><a [href]="link('en')">English</a></li>
        <li><a [href]="link('nl')">Nederlands</a></li>
        <li><a [href]="link('fr')">Français</a></li>
        <li><a [href]="link('de')">Deutsch</a></li>
      </ul>

      <p class="version">
        v3.0.0-alpha-31
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
      padding-top: 15px;
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
