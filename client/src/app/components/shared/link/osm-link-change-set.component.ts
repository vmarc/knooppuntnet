import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-osm-link-change-set",
  template: `
    <kpn-osm-link kind="changeset" [elementId]="changeSetId.toString()" title="osm"></kpn-osm-link>
  `
})
export class OsmLinkChangeSetComponent {

  @Input() changeSetId: number;

}
