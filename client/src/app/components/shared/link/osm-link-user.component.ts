import {Component, Input} from "@angular/core";

@Component({
  selector: "osm-link-user",
  template: `
    <osm-link kind="user" id="{{user}}" title="{{user}}"></osm-link>
  `
})
export class OsmLinkUserComponent {
  @Input() user;
}
