import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {NetworkChangeInfo} from "../../kpn/shared/changes/details/network-change-info";

@Component({
  selector: "kpn-change-set-network-change-nodes-added",
  template: `
    <div *ngIf="!nodeIds().isEmpty()" class="kpn-level-2">
      <div class="kpn-level-2-header kpn-line">
        <!-- @@ Toegevoegde nodes die geen knooppunten zijn -->
        <span i18n="@@change-set.network-changes.added-nodes">Added non-network nodes</span>
        <span class="kpn-thin">{{nodeIds().size}}</span>
        <kpn-icon-investigate></kpn-icon-investigate>
      </div>
      <div class="kpn-level-2-body kpn-comma-list">
        <kpn-osm-link-node *ngFor="let nodeId of nodeIds()" [nodeId]="nodeId" [title]="nodeId.toString()"></kpn-osm-link-node>
      </div>
    </div>
  `
})
export class ChangeSetNetworkChangeNodesAddedComponent {

  @Input() networkChangeInfo: NetworkChangeInfo;

  nodeIds(): List<number> {
    return this.networkChangeInfo.nodes.added;
  }

}
