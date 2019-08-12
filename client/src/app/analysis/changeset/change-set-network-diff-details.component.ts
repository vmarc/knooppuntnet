import {Component, Input} from "@angular/core";
import {ChangeSetPage} from "../../kpn/shared/changes/change-set-page";

@Component({
  selector: "kpn-change-set-network-diff-details",
  template: `
    <div *ngFor="let networkChangeInfo of page.networkChanges">

      <a [id]="networkChangeInfo.networkId"></a>
      <div class="kpn-line">
        <kpn-network-type-icon [networkType]="networkChangeInfo.networkType"></kpn-network-type-icon>
        <span i18n="@@change-set.network-diffs.network">Network</span>
        <kpn-link-network-details [networkId]="networkChangeInfo.networkId" [title]="networkChangeInfo.networkName"></kpn-link-network-details>
      </div>

      <div *ngIf="networkChangeInfo.changeType.name == 'create'">
        <b i18n="@@change-set.network-diffs.network-created">
          Network created
        </b>
      </div>
      <div *ngIf="networkChangeInfo.changeType.name == 'delete'">
        <b i18n="@@change-set.network-diffs.network-deleted">
          Network deleted
        </b>
      </div>
      <!-- no changeType text for "Updated" -->


      <div *ngIf="!networkChangeInfo.orphanRoutes.oldRefs.isEmpty()" class="kpn-line">
        <span i18n="@@change-set.network-diffs.orphan-routes-resolved">
          Orphan routes added to this network
        </span>
        <div class="kpn-comma-list">
          <kpn-link-route
            *ngFor="let ref of networkChangeInfo.orphanRoutes.oldRefs"
            [routeId]="ref.id"
            [title]="ref.name">
          </kpn-link-route>
        </div>
        <mat-icon svgIcon="happy"></mat-icon>
      </div>


      <div *ngIf="!networkChangeInfo.orphanRoutes.newRefs.isEmpty()" class="kpn-line">
        <span i18n="@@change-set.network-diffs.orphan-routes-introduced">
          Following routes that used to be part of this network have become orphan
        </span>
        <div class="kpn-comma-list">
          <kpn-link-route
            *ngFor="let ref of networkChangeInfo.orphanRoutes.newRefs"
            [routeId]="ref.id"
            [title]="ref.name">
          </kpn-link-route>
        </div>
        <mat-icon svgIcon="investigate"></mat-icon>
      </div>


      <div *ngIf="!networkChangeInfo.orphanNodes.oldRefs.isEmpty()" class="kpn-line">
        <span i18n="@@change-set.network-diffs.orphan-nodes-resolved">
          Orphan nodes added to this network
        </span>
        <div class="kpn-comma-list">
          <kpn-link-node
            *ngFor="let ref of networkChangeInfo.orphanNodes.oldRefs"
            [nodeId]="ref.id"
            [nodeName]="ref.name">
          </kpn-link-node>
        </div>
        <mat-icon svgIcon="happy"></mat-icon>
      </div>


      <div *ngIf="!networkChangeInfo.orphanNodes.newRefs.isEmpty()" class="kpn-line">
        <span i18n="@@change-set.network-diffs.orphan-nodes-introduced">
          Following nodes that used to be part of this network have become orphan
        </span>
        <div class="kpn-comma-list">
          <kpn-link-node
            *ngFor="let ref of networkChangeInfo.orphanNodes.newRefs"
            [nodeId]="ref.id"
            [nodeName]="ref.name">
          </kpn-link-node>
        </div>
        <mat-icon svgIcon="investigate"></mat-icon>
      </div>

      <div>TODO UiNetworkDiff(changeSetSummary, networkChangeInfo, routeChangeInfos, nodeChangeInfos, knownElements)</div>

    </div>
  `
})
export class ChangeSetNetworkDiffDetailsComponent {
  @Input() page: ChangeSetPage;
}
