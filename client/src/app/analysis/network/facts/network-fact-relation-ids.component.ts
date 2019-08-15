import {Component, Input} from "@angular/core";
import {List} from "immutable";

@Component({
  selector: "kpn-network-fact-relation-ids",
  template: `
    <div *ngFor="let elementId of elementIds">
      <kpn-osm-link-relation [relationId]="elementId" [title]="elementId.toString()"></kpn-osm-link-relation>
      <span class="kpn-brackets">
        <kpn-josm-relation [relationId]="elementId"></kpn-josm-relation>
      </span>
    </div>
  `
})
export class NetworkFactRelationIdsComponent {
  @Input() elementIds: List<number>;
}
