import {Component, Input} from "@angular/core";

@Component({
  selector: 'osm-link-relation',
  template: `
    <osm-link kind="relation" id="{{relationId}}" title="osm"></osm-link>
  `
})
export class OsmLinkRelationComponent {

  @Input() relationId = "";

}
