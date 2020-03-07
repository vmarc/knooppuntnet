import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {NetworkChangeInfo} from "../../../../kpn/api/common/changes/details/network-change-info";

@Component({
  selector: "kpn-cs-nc-ways-updated",
  template: `
    <div *ngIf="!wayIds().isEmpty()" class="kpn-level-2">
      <div class="kpn-level-2-header kpn-line">
        <!-- @@ Aangepaste wegen -->
        <span i18n="@@change-set.network-changes.updated-ways">Updated ways</span>
        <span class="kpn-thin">{{wayIds().size}}</span>
      </div>
      <div class="kpn-level-2-body kpn-comma-list">
        <kpn-osm-link-way *ngFor="let wayId of wayIds()" [wayId]="wayId" [title]="wayId.toString()"></kpn-osm-link-way>
      </div>
    </div>
  `
})
export class CsNcWaysUpdatedComponent {

  @Input() networkChangeInfo: NetworkChangeInfo;

  wayIds(): List<number> {
    return this.networkChangeInfo.ways.updated;
  }

}
