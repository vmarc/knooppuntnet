import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-osm-link-user",
  template: `
    <kpn-osm-link kind="user" id="{{user}}" title="{{user}}"></kpn-osm-link>
  `
})
export class OsmLinkUserComponent {
  @Input() user;
}
