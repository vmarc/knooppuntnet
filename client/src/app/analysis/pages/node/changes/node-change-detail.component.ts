import {Component, Input} from "@angular/core";
import {NodeChangeInfo} from "../../../../kpn/shared/node/node-change-info";

@Component({
  selector: "kpn-node-change-detail",
  template: `

    <!-- ** facts -->
    <div *ngFor="let fact of nodeChangeInfo.facts" class="kpn-detail">
      <kpn-fact-name [factName]="fact.name"></kpn-fact-name>
    </div>


    <!-- ** connectionChanges -->
    <div *ngFor="let change of nodeChangeInfo.connectionChanges" class="kpn-detail">
      <span *ngIf="change.after" i18n="@@node-change.belongs-to-another-network">This node belongs to another network</span>
      <!--@@ Dit knooppunt behoort tot een ander netwerk. -->
      <span *ngIf="!change.after" i18n="@@node-change.no-longer-belongs-to-another-network">This node no longer belongs to another network</span>
      <!--@@ Dit knooppunt behoort niet langer tot een ander netwerk. -->
      : <a routerLink="{{'/analysis/network/' + change.ref.id}}">{{change.ref.name}}</a>.
    </div>


    <!-- ** roleConnectionChanges -->
    <div *ngFor="let change of nodeChangeInfo.roleConnectionChanges" class="kpn-detail">
      <span *ngIf="change.after" i18n="@@node-change.received-role-connection-in-network-relation">This node received role "connection" in the network relation</span>
      <!--@@ Dit knooppunt kreeg rol "connection" in de netwerk relatie -->
      <span *ngIf="!change.after" i18n="@@node-change.lost-role-connection-in-network-relation">This node no longer has role "connection" in the network relation</span>
      <!--@@ Dit knooppunt heeft niet langer de rol "connectinon" in de netwerk relatie -->
      : <a routerLink="{{'/analysis/network/' + change.ref.id}}">{{change.ref.name}}</a>.
    </div>
    
    
    <!-- ** definedInNetworkChanges -->
    <div *ngFor="let change of nodeChangeInfo.definedInNetworkChanges" class="kpn-detail">
      <span *ngIf="change.after" i18n="@@node-change.added-to-network-relation">Added to network relation</span>
      <!--@@ Toegevoegd aan netwerkrelatie -->
      <span *ngIf="!change.after" i18n="@@node-change.removed-from-network-relation">Removed from network relation</span>
      <!--@@ Verwijderd uit netwerkrelatie van -->
      : <a routerLink="{{'/analysis/network/' + change.ref.id}}">{{change.ref.name}}</a>.
    </div>
    

    <!-- ** nodeChange.addedToRoute -->
    <div *ngFor="let ref of nodeChangeInfo.addedToRoute" class="kpn-detail">
      <ng-container i18n="@@node-change.added-to-route">Added to route</ng-container>
      : <!--@@ Toegevoegd aan route -->
      <a routerLink="{{'/analysis/route/' + ref.id}}">{{ref.name}}</a>.
    </div>

    
    <!-- ** nodeChange.addedToNetwork -->
    <div *ngFor="let ref of nodeChangeInfo.addedToNetwork" class="kpn-detail">
      <ng-container i18n="@@node-change.added-to-network">Added to network</ng-container>
      : <!--@@ Toegevoegd aan netwerk -->
      <a routerLink="{{'/analysis/network/' + ref.id}}">{{ref.name}}</a>.
    </div>


    <!-- ** nodeChange.removedFromRoute -->
    <div *ngFor="let ref of nodeChangeInfo.removedFromRoute" class="kpn-detail">
      <ng-container i18n="@@node-change.removed-from-route">Removed from route</ng-container>
      : <!--@@ Verwijderd uit route -->
      <a routerLink="{{'/analysis/route/' + ref.id}}">{{ref.name}}</a>.
    </div>


    <!-- ** nodeChange.removedFromNetwork -->
    <div *ngFor="let ref of nodeChangeInfo.removedFromNetwork" class="kpn-detail">
      <ng-container i18n="@@node-change.removed-from-network">Removed from network</ng-container>
      : <!--@@ Verwijderd uit netwerk -->
      <a routerLink="{{'/analysis/network/' + ref.id}}">{{ref.name}}</a>.
    </div>


    <!-- ** factDiffs -->
    <kpn-fact-diffs [factDiffs]="nodeChangeInfo.factDiffs"></kpn-fact-diffs>
    

    <!-- ** tagDiffs -->
    <div *ngIf="nodeChangeInfo.tagDiffs" class="kpn-detail">
      <kpn-tag-diffs [tagDiffs]="nodeChangeInfo.tagDiffs"></kpn-tag-diffs>
    </div>

    <kpn-node-change-moved [nodeChangeInfo]="nodeChangeInfo"></kpn-node-change-moved>

  `
})
export class NodeChangeDetailComponent {
  @Input() nodeChangeInfo: NodeChangeInfo;
}
