import {Component, Input} from "@angular/core";
import {List} from "immutable";

@Component({
  selector: "kpn-network-fact-node-ids",
  template: `
    <div *ngFor="let elementId of elementIds">
      <kpn-osm-link-node [nodeId]="elementId" [title]="elementId.toString()"></kpn-osm-link-node>
      <span class="kpn-brackets">
        <kpn-josm-node [nodeId]="elementId"></kpn-josm-node>
      </span>
    </div>
  `
})
export class NetworkFactNodeIdsComponent {
  @Input() elementIds: List<number>;
}
