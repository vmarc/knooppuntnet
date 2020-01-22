import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-osm-link-node",
  template: `
    <kpn-osm-link kind="node" [id]="nodeId" [title]="title"></kpn-osm-link>
  `
})
export class OsmLinkNodeComponent {
  @Input() nodeId: number;
  @Input() title = "osm";
}
