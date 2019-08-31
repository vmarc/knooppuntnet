import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {NetworkChangeInfo} from "../../../kpn/shared/changes/details/network-change-info";

@Component({
  selector: "kpn-cs-nc-relations-updated",
  template: `
    <div *ngIf="!relationIds().isEmpty()" class="kpn-level-2">
      <div class="kpn-level-2-header kpn-line">
        <!-- @@ Aangepaste relaties die geen route relatie zijn -->
        <span i18n="@@change-set.network-changes.updated-relations">Updated non-route relations</span>
        <span class="kpn-thin">{{relationIds().size}}</span>
      </div>
      <div class="kpn-level-2-body kpn-comma-list">
        <kpn-osm-link-relation
          *ngFor="let relationId of relationIds()"
          [relationId]="relationId"
          [title]="relationId.toString()"></kpn-osm-link-relation>
      </div>
    </div>
  `
})
export class CsNcRelationsUpdatedComponent {

  @Input() networkChangeInfo: NetworkChangeInfo;

  relationIds(): List<number> {
    return this.networkChangeInfo.relations.updated;
  }

}
