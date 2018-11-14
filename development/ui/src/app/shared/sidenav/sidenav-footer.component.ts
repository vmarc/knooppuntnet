import {Component} from '@angular/core';
import {UserService} from "../../user.service";

@Component({
  selector: 'kpn-sidenav-footer',
  template: `
    <div class="footer">
      <p>
        <kpn-link-about></kpn-link-about>
        |
        <kpn-link-glossary></kpn-link-glossary>
        |
        <kpn-link-links></kpn-link-links>
      </p>
      <p>
        <kpn-link-overview></kpn-link-overview>
      </p>
      <p>
        <a>English</a>
        |
        <a>Nederlands</a>
      </p>
      <p>
        <a href="https://www.openstreetmap.org/message/new/vmarc" class="external" target="_blank">Contact</a>
        |
        <a href="https://github.com/vmarc/knooppuntnet/issues" class="external" target="_blank">Issues</a>
      </p>

      <p class="version">
        version
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
export class SidenavFooterComponent {

  constructor(private userService: UserService) {
  }

  currentUser(): string {
    return this.userService.currentUser();
  }

  isLoggedIn(): boolean {
    return this.currentUser().length > 0;
  }

}
