import {Component, Input} from '@angular/core';
import {ChangeSetPage} from "../../../kpn/shared/changes/change-set-page";

@Component({
  selector: 'kpn-change-set-network-diff-details',
  template: `
    <div *ngFor="let networkChangeInfo of page.networkChanges">

      <a [id]="networkChangeInfo.networkId"></a>
      <div class="kpn-line">
        <kpn-network-type-icon [networkType]="networkChangeInfo.networkType"></kpn-network-type-icon>
        <span>Network</span>
        <!-- Netwerk -->
        <kpn-link-network-details [networkId]="networkChangeInfo.networkId" [title]="networkChangeInfo.networkName"></kpn-link-network-details>
      </div>

      <div *ngIf="networkChangeInfo.changeType.name == 'create'">
        <b>
          Network created
          <!-- Netwerk nieuw aangemaakt -->
        </b>
      </div>
      <div *ngIf="networkChangeInfo.changeType.name == 'delete'">
        <b>
          Network deleted
          <!-- Netwerk verwijderd -->
        </b>
      </div>
      <!-- no changeType text for "Updated" -->


      <div *ngIf="!networkChangeInfo.orphanRoutes.oldRefs.isEmpty()" class="kpn-line">
        <span>Orphan routes added to this network</span>
        <!-- Route wezen toegevoegd aan dit netwerk -->
        <div class="kpn-comma-list">
          <kpn-link-route
            *ngFor="let ref of networkChangeInfo.orphanRoutes.oldRefs"
            [routeId]="ref.id"
            [title]="ref.name">
          </kpn-link-route>
        </div>
        <mat-icon>sentiment_satisfied_alt</mat-icon>
      </div>


      <div *ngIf="!networkChangeInfo.orphanRoutes.newRefs.isEmpty()" class="kpn-line">
        <span>Following routes that used to be part of this network have become orphan</span>
        <!-- Volgende routes maakten deel uit van dit netwerk, maar zijn nu route wees geworden -->
        <div class="kpn-comma-list">
          <kpn-link-route
            *ngFor="let ref of networkChangeInfo.orphanRoutes.newRefs"
            [routeId]="ref.id"
            [title]="ref.name">
          </kpn-link-route>
        </div>
        <mat-icon>error_outline</mat-icon>
      </div>


      <div *ngIf="!networkChangeInfo.orphanNodes.oldRefs.isEmpty()" class="kpn-line">
        <span>Orphan nodes added to this network</span>
        <!-- Knooppuntwezen toegevoegd aan dit netwerk -->
        <div class="kpn-comma-list">
          <kpn-link-node
            *ngFor="let ref of networkChangeInfo.orphanNodes.oldRefs"
            [nodeId]="ref.id"
            [title]="ref.name">
          </kpn-link-node>
        </div>
        <mat-icon>sentiment_satisfied_alt</mat-icon>
      </div>


      <div *ngIf="!networkChangeInfo.orphanNodes.newRefs.isEmpty()" class="kpn-line">
        <span>Following nodes that used to be part of this network have become orphan</span>
        <!-- Volgende knooppunten maakten tot nu toe deel uit van dit netwerk, maar zijn knooppunt wees geworden -->
        <div class="kpn-comma-list">
          <kpn-link-node
            *ngFor="let ref of networkChangeInfo.orphanNodes.newRefs"
            [nodeId]="ref.id"
            [title]="ref.name">
          </kpn-link-node>
        </div>
        <mat-icon>error_outline</mat-icon>
      </div>

      <div>TODO UiNetworkDiff(changeSetSummary, networkChangeInfo, routeChangeInfos, nodeChangeInfos, knownElements)</div>

    </div>
  `
})
export class ChangeSetNetworkDiffDetailsComponent {
  @Input() page: ChangeSetPage;
}
