import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-osm-link-way",
  template: `
    <kpn-osm-link kind="way" [elementId]="wayId.toString()" [title]="title"></kpn-osm-link>
  `
})
export class OsmLinkWayComponent {
  @Input() wayId: number;
  @Input() title = "osm";
}
