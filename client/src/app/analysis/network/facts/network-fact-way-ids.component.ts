import {Component, Input} from "@angular/core";
import {List} from "immutable";

@Component({
  selector: "kpn-network-fact-way-ids",
  template: `
    <div *ngFor="let elementId of elementIds">
      <kpn-osm-link-way [wayId]="elementId" [title]="elementId.toString()"></kpn-osm-link-way>
      <span class="kpn-brackets">
        <kpn-josm-way [wayId]="elementId"></kpn-josm-way>
      </span>
    </div>
  `
})
export class NetworkFactWayIdsComponent {
  @Input() elementIds: List<number>;
}
