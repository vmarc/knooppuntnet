import {Component} from "@angular/core";

@Component({
  selector: "kpn-link-logout",
  template: `
    <a routerLink="/logout" i18n="@@link.logout">logout</a>
  `
})
export class LinkLogoutComponent {
}
