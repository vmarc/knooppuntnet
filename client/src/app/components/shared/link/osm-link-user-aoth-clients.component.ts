import {Component, Input} from "@angular/core";

@Component({
  selector: "osm-link-user-oath-clients",
  template: `
    <a
      class="external"
      href="https://www.openstreetmap.org/user/{{user}}/oauth_clients"
      rel="nofollow"
      target="_blank">
      {{title}}
    </a>
  `
})
export class OsmLinkUserAothClientsComponent {

  @Input() user;
  @Input() title;

}
