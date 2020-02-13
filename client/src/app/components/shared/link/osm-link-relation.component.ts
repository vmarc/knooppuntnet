import {Component, Input} from "@angular/core";
import {OnInit} from "@angular/core";

@Component({
  selector: "kpn-osm-link-relation",
  template: `
    <kpn-osm-link kind="relation" [elementId]="relationId.toString()" [title]="title"></kpn-osm-link>
  `
})
export class OsmLinkRelationComponent {
  @Input() relationId: number;
  @Input() title = "osm";
}
