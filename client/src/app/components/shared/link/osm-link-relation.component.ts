import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-osm-link-relation",
  template: `
    <kpn-osm-link kind="relation" id="{{relationId}}" [title]="title"></kpn-osm-link>
  `
})
export class OsmLinkRelationComponent {
  @Input() relationId: number;
  @Input() title = "osm";
}
