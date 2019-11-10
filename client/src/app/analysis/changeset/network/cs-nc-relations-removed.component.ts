import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {NetworkChangeInfo} from "../../../kpn/api/common/changes/details/network-change-info";

@Component({
  selector: "kpn-cs-nc-relations-removed",
  template: `
    <div *ngIf="!relationIds().isEmpty()" class="kpn-level-2">
      <div class="kpn-level-2-header kpn-line">
        <!-- @@ Verwijderde relaties die geen route relatie zijn -->
        <span i18n="@@change-set.network-changes.removed-relations">Removed non-route relations</span>
        <span class="kpn-thin">{{relationIds().size}}</span>
        <kpn-icon-happy></kpn-icon-happy>
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
export class CsNcRelationsRemovedComponent {

  @Input() networkChangeInfo: NetworkChangeInfo;

  relationIds(): List<number> {
    return this.networkChangeInfo.relations.removed;
  }

}
