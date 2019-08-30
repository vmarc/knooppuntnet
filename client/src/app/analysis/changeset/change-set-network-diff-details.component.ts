import {Component, Input} from "@angular/core";
import {ChangeSetPage} from "../../kpn/shared/changes/change-set-page";

@Component({
  selector: "kpn-change-set-network-diff-details",
  template: `
    <div *ngFor="let networkChangeInfo of page.networkChanges" class="kpn-level-1">

      <div class="kpn-level-1-header">
        <a [id]="networkChangeInfo.networkId"></a>
        <div class="kpn-line">
          <kpn-network-type-icon [networkType]="networkChangeInfo.networkType"></kpn-network-type-icon>
          <span i18n="@@change-set.network-diffs.network">Network</span>
          <kpn-link-network-details [networkId]="networkChangeInfo.networkId" [title]="networkChangeInfo.networkName"></kpn-link-network-details>
        </div>
      </div>

      <div class="kpn-level-1-body">

        <div *ngIf="networkChangeInfo.changeType.name == 'Create'" class="kpn-detail">
          <b i18n="@@change-set.network-diffs.network-created">
            Network created
          </b>
        </div>
        <div *ngIf="networkChangeInfo.changeType.name == 'Delete'" class="kpn-detail">
          <b i18n="@@change-set.network-diffs.network-deleted">
            Network deleted
          </b>
        </div>
        <!-- no changeType text for "Update" -->


        <div *ngIf="!networkChangeInfo.orphanRoutes.oldRefs.isEmpty()" class="kpn-detail kpn-line">
          <span i18n="@@change-set.network-diffs.orphan-routes-resolved" class="kpn-label">
            Orphan routes added to this network
          </span>
          <div class="kpn-comma-list">
            <kpn-link-route-ref
              *ngFor="let ref of networkChangeInfo.orphanRoutes.oldRefs"
              [ref]="ref"
              [knownElements]="page.knownElements">
            </kpn-link-route-ref>
          </div>
          <kpn-icon-happy></kpn-icon-happy>
        </div>


        <div *ngIf="!networkChangeInfo.orphanRoutes.newRefs.isEmpty()" class="kpn-detail kpn-line">
          <span i18n="@@change-set.network-diffs.orphan-routes-introduced" class="kpn-label">
            Following routes that used to be part of this network have become orphan
          </span>
          <div class="kpn-comma-list">
            <kpn-link-route-ref
              *ngFor="let ref of networkChangeInfo.orphanRoutes.newRefs"
              [ref]="ref"
              [knownElements]="page.knownElements">
            </kpn-link-route-ref>
          </div>
          <kpn-icon-investigate></kpn-icon-investigate>
        </div>


        <div *ngIf="!networkChangeInfo.orphanNodes.oldRefs.isEmpty()" class="kpn-detail kpn-line">
          <span i18n="@@change-set.network-diffs.orphan-nodes-resolved" class="kpn-label">
            Orphan nodes added to this network
          </span>
          <div class="kpn-comma-list">
            <kpn-link-node-ref
              *ngFor="let ref of networkChangeInfo.orphanNodes.oldRefs"
              [ref]="ref"
              [knownElements]="page.knownElements">
            </kpn-link-node-ref>
          </div>
          <kpn-icon-happy></kpn-icon-happy>
        </div>


        <div *ngIf="!networkChangeInfo.orphanNodes.newRefs.isEmpty()" class="kpn-detail kpn-line">
          <span i18n="@@change-set.network-diffs.orphan-nodes-introduced" class="kpn-label">
            Following nodes that used to be part of this network have become orphan
          </span>
          <div class="kpn-comma-list">
            <kpn-link-node-ref
              *ngFor="let ref of networkChangeInfo.orphanNodes.newRefs"
              [ref]="ref"
              [knownElements]="page.knownElements">
            </kpn-link-node-ref>
          </div>
          <kpn-icon-investigate></kpn-icon-investigate>
        </div>

        <div>TODO UiNetworkDiff(changeSetSummary, networkChangeInfo, routeChangeInfos, nodeChangeInfos, knownElements)</div>

      </div>
    </div>
  `
})
export class ChangeSetNetworkDiffDetailsComponent {
  @Input() page: ChangeSetPage;
}
