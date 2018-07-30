import {Component, Input} from "@angular/core";

@Component({
  selector: 'osm-link-node',
  template: `
    <osm-link kind="node" id="{{nodeId}}" title="osm"></osm-link>
  `
})
export class OsmLinkNodeComponent {

  @Input() nodeId = "";

}
