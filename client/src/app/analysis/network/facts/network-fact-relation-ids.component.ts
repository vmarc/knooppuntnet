import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {List} from "immutable";

@Component({
  selector: "kpn-network-fact-relation-ids",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngFor="let elementId of elementIds">
      <kpn-osm-link-relation [relationId]="elementId" [title]="elementId.toString()"></kpn-osm-link-relation>
      <span class="kpn-brackets-link">
        <kpn-josm-relation [relationId]="elementId"></kpn-josm-relation>
      </span>
    </div>
  `
})
export class NetworkFactRelationIdsComponent {
  @Input() elementIds: List<number>;
}
