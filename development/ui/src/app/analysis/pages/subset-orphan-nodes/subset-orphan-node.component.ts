import {Component, Input} from '@angular/core';
import {NodeInfo} from "../../../kpn/shared/node-info";

@Component({
  selector: 'kpn-subset-orphan-node',
  template: `
    <p>
      <kpn-link-node [nodeId]="node.id" [title]="node.name"></kpn-link-node>
    </p>
    <p>
      <timestamp [timestamp]="node.lastUpdated"></timestamp>
    </p>
    <p *ngIf="extraTags()">
      <span>Extra tags:</span>
      <tags [tags]="extraTags()"></tags>
    </p>
    <p>
      <osm-link-node nodeId="{{node.id}}"></osm-link-node>
      <josm-node nodeId="{{node.id}}"></josm-node>
    </p>
  `
})
export class SubsetOrphanNodeComponent {

  @Input() node: NodeInfo;

  extraTags() {
    // TODO Tags(RouteTagFilter(route).extraTags)
    return this.node.tags;
  }

}
