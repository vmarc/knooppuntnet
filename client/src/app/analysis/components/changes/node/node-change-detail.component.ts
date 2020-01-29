import {Component, Input} from "@angular/core";
import {NodeChangeInfo} from "../../../../kpn/api/common/node/node-change-info";

@Component({
  selector: "kpn-node-change-detail",
  template: `

    <!-- facts -->
    <div *ngFor="let fact of nodeChangeInfo.facts" class="kpn-detail">
      <kpn-fact-name [factName]="fact.name"></kpn-fact-name>
    </div>


    <!-- connectionChanges -->
    <div *ngFor="let change of nodeChangeInfo.connectionChanges" class="kpn-detail">
      <span *ngIf="change.after" class="kpn-label" i18n="@@node-change.belongs-to-another-network">
        This node belongs to another network
      </span>
      <span *ngIf="!change.after" class="kpn-label" i18n="@@node-change.no-longer-belongs-to-another-network">
        This node no longer belongs to another network
      </span>
      <kpn-link-network-details [networkId]="change.ref.id" [title]="change.ref.name"></kpn-link-network-details>
    </div>


    <!--roleConnectionChanges -->
    <div *ngFor="let change of nodeChangeInfo.roleConnectionChanges" class="kpn-detail">
      <span *ngIf="change.after" class="kpn-label" i18n="@@node-change.received-role-connection-in-network-relation">
        This node received role "connection" in the network relation
      </span>
      <span *ngIf="!change.after" class="kpn-label" i18n="@@node-change.lost-role-connection-in-network-relation">
        This node no longer has role "connection" in the network relation
      </span>
      <kpn-link-network-details [networkId]="change.ref.id" [title]="change.ref.name"></kpn-link-network-details>
    </div>


    <!-- definedInNetworkChanges -->
    <div *ngFor="let change of nodeChangeInfo.definedInNetworkChanges" class="kpn-detail">
      <span *ngIf="change.after" class="kpn-label" i18n="@@node-change.added-to-network-relation">
        Added to network relation
      </span>
      <span *ngIf="!change.after" class="kpn-label" i18n="@@node-change.removed-from-network-relation">
        Removed from network relation
      </span>
      <kpn-link-network-details [networkId]="change.ref.id" [title]="change.ref.name"></kpn-link-network-details>
    </div>


    <div *ngFor="let ref of nodeChangeInfo.addedToRoute" class="kpn-detail">
      <ng-container class="kpn-label" i18n="@@node-change.added-to-route">Added to route</ng-container>
      <kpn-link-route [routeId]="ref.id" [title]="ref.name"></kpn-link-route>
    </div>


    <div *ngFor="let ref of nodeChangeInfo.addedToNetwork" class="kpn-detail">
      <ng-container i18n="@@node-change.added-to-network">Added to network</ng-container>
      <kpn-link-network-details [networkId]="ref.id" [title]="ref.name"></kpn-link-network-details>
    </div>


    <div *ngFor="let ref of nodeChangeInfo.removedFromRoute" class="kpn-detail">
      <ng-container class="kpn-label" i18n="@@node-change.removed-from-route">Removed from route</ng-container>
      <kpn-link-route [routeId]="ref.id" [title]="ref.name"></kpn-link-route>
    </div>


    <div *ngFor="let ref of nodeChangeInfo.removedFromNetwork" class="kpn-detail">
      <ng-container class="kpn-label" i18n="@@node-change.removed-from-network">Removed from network</ng-container>
      <kpn-link-network-details [networkId]="ref.id" [title]="ref.name"></kpn-link-network-details>
    </div>

    <kpn-fact-diffs [factDiffs]="nodeChangeInfo.factDiffs"></kpn-fact-diffs>

    <div *ngIf="nodeChangeInfo.tagDiffs" class="kpn-detail">
      <kpn-tag-diffs [tagDiffs]="nodeChangeInfo.tagDiffs"></kpn-tag-diffs>
    </div>

    <kpn-node-change-moved [nodeChangeInfo]="nodeChangeInfo"></kpn-node-change-moved>

  `
})
export class NodeChangeDetailComponent {
  @Input() nodeChangeInfo: NodeChangeInfo;
}
