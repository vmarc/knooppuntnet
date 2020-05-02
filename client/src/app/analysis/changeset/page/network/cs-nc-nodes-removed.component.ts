import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {NetworkChangeInfo} from "../../../../kpn/api/common/changes/details/network-change-info";

@Component({
  selector: "kpn-cs-nc-nodes-removed",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="!nodeIds().isEmpty()" class="kpn-level-2">
      <div class="kpn-level-2-header kpn-line">
        <!-- @@ Verwijderde nodes die geen knooppunten zijn -->
        <span i18n="@@change-set.network-changes.removed-nodes">Removed non-network nodes</span>
        <span class="kpn-thin">{{nodeIds().size}}</span>
        <kpn-icon-happy></kpn-icon-happy>
      </div>
      <div class="kpn-level-2-body kpn-comma-list">
        <kpn-osm-link-node
          *ngFor="let nodeId of nodeIds()"
          [nodeId]="nodeId"
          [title]="nodeId.toString()">
        </kpn-osm-link-node>
      </div>
    </div>
  `
})
export class CsNcNodesRemovedComponent {

  @Input() networkChangeInfo: NetworkChangeInfo;

  nodeIds(): List<number> {
    return this.networkChangeInfo.nodes.removed;
  }
}
