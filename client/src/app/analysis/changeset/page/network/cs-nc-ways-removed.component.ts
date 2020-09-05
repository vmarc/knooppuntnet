import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {NetworkChangeInfo} from "../../../../kpn/api/common/changes/details/network-change-info";

@Component({
  selector: "kpn-cs-nc-ways-removed",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="!wayIds().isEmpty()" class="kpn-level-2">
      <div class="kpn-level-2-header kpn-line">
        <span i18n="@@change-set.network-changes.removed-ways">Removed ways</span>
        <span class="kpn-brackets kpn-thin">{{wayIds().size}}</span>
        <kpn-icon-happy></kpn-icon-happy>
      </div>
      <div class="kpn-level-2-body kpn-comma-list">
        <kpn-osm-link-way *ngFor="let wayId of wayIds()" [wayId]="wayId" [title]="wayId.toString()"></kpn-osm-link-way>
      </div>
    </div>
  `
})
export class CsNcWaysRemovedComponent {

  @Input() networkChangeInfo: NetworkChangeInfo;

  wayIds(): List<number> {
    return this.networkChangeInfo.ways.removed;
  }

}
