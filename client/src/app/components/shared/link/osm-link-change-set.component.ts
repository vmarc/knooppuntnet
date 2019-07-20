import {Component, Input} from "@angular/core";

@Component({
  selector: "osm-link-change-set",
  template: `
    <osm-link kind="changeset" id="{{changeSetId}}" title="osm"></osm-link>
  `
})
export class OsmLinkChangeSetComponent {

  @Input() changeSetId: number;

}
