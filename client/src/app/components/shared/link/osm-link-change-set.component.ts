import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-osm-link-change-set",
  template: `
    <kpn-osm-link kind="changeset" id="{{changeSetId}}" title="osm"></kpn-osm-link>
  `
})
export class OsmLinkChangeSetComponent {

  @Input() changeSetId: number;

}
