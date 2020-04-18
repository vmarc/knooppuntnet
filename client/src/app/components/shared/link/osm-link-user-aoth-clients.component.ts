import {Component} from "@angular/core";
import {UserService} from "../../../services/user.service";

@Component({
  selector: "kpn-osm-link-user-oath-clients",
  template: `
    <a
      class="external"
      href="https://www.openstreetmap.org/user/{{currentUser()}}/oauth_clients"
      rel="nofollow noreferrer"
      target="_blank" i18n="@@osm-link.oath-clients">
      list of authorised applications
    </a>
  `
})
export class OsmLinkUserAothClientsComponent {

  constructor(private userService: UserService) {
  }

  currentUser(): string {
    return this.userService.currentUser();
  }

}
