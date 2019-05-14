import {Component} from "@angular/core";
import {UserService} from "../../../services/user.service";

@Component({
  selector: "kpn-sidebar-footer",
  template: `
    <kpn-sidebar-version-warning></kpn-sidebar-version-warning>
    <div class="footer">
      <p>
        <a>English</a>
        |
        <a>Nederlands</a>
      </p>

      <p class="version">
        2019-05-13
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

  constructor(private userService: UserService) {
  }

  currentUser(): string {
    return this.userService.currentUser();
  }

  isLoggedIn(): boolean {
    return this.currentUser().length > 0;
  }

}
