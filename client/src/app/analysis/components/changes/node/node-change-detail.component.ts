import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NodeMoved } from '@api/common/diff/node/node-moved';
import { NodeChangeInfo } from '@api/common/node/node-change-info';
import { InterpretedTags } from '@app/components/shared/tags/interpreted-tags';
import { Util } from '@app/components/shared/util';

@Component({
  selector: 'kpn-node-change-detail',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngFor="let fact of nodeChangeInfo.facts" class="kpn-detail">
      <kpn-fact-name [fact]="fact" />
    </div>

    <div
      *ngFor="let change of nodeChangeInfo.connectionChanges"
      class="kpn-detail"
    >
      <span
        *ngIf="change.after"
        i18n="@@node-change.belongs-to-another-network"
        class="kpn-label"
        >This node belongs to another network</span
      >
      <span
        *ngIf="!change.after"
        i18n="@@node-change.no-longer-belongs-to-another-network"
        class="kpn-label"
        >This node no longer belongs to another network</span
      >
      <kpn-link-network-details
        [networkId]="change.ref.id"
        [title]="change.ref.name"
      />
    </div>

    <div
      *ngFor="let change of nodeChangeInfo.roleConnectionChanges"
      class="kpn-detail"
    >
      <span
        *ngIf="change.after"
        i18n="@@node-change.received-role-connection-in-network-relation"
        class="kpn-label"
        >This node received role "connection" in the network relation</span
      >
      <span
        *ngIf="!change.after"
        i18n="@@node-change.lost-role-connection-in-network-relation"
        class="kpn-label"
        >This node no longer has role "connection" in the network relation</span
      >
      <kpn-link-network-details
        [networkId]="change.ref.id"
        [title]="change.ref.name"
      />
    </div>

    <div
      *ngFor="let change of nodeChangeInfo.definedInNetworkChanges"
      class="kpn-detail"
    >
      <span
        *ngIf="change.after"
        i18n="@@node-change.added-to-network-relation"
        class="kpn-label"
        >Added to network relation</span
      >
      <span
        *ngIf="!change.after"
        i18n="@@node-change.removed-from-network-relation"
        class="kpn-label"
        >Removed from network relation</span
      >
      <kpn-link-network-details
        [networkId]="change.ref.id"
        [title]="change.ref.name"
      />
    </div>

    <div *ngFor="let ref of nodeChangeInfo.addedToRoute" class="kpn-detail">
      <span i18n="@@node-change.added-to-route" class="kpn-label"
        >Added to route</span
      >
      <kpn-link-route [routeId]="ref.id" [title]="ref.name" />
    </div>

    <div *ngFor="let ref of nodeChangeInfo.addedToNetwork" class="kpn-detail">
      <span i18n="@@node-change.added-to-network" class="kpn-label"
        >Added to network</span
      >
      <kpn-link-network-details [networkId]="ref.id" [title]="ref.name" />
    </div>

    <div *ngFor="let ref of nodeChangeInfo.removedFromRoute" class="kpn-detail">
      <span i18n="@@node-change.removed-from-route" class="kpn-label"
        >Removed from route</span
      >
      <kpn-link-route [routeId]="ref.id" [title]="ref.name" />
    </div>

    <div
      *ngFor="let ref of nodeChangeInfo.removedFromNetwork"
      class="kpn-detail"
    >
      <span i18n="@@node-change.removed-from-network" class="kpn-label"
        >Removed from network</span
      >
      <kpn-link-network-details [networkId]="ref.id" [title]="ref.name" />
    </div>

    <kpn-fact-diffs [factDiffs]="nodeChangeInfo.factDiffs" />

    <div *ngIf="hasTagDiffs()" class="kpn-detail">
      <kpn-tag-diffs [tagDiffs]="nodeChangeInfo.tagDiffs" />
    </div>

    <kpn-node-change-moved [nodeChangeInfo]="nodeChangeInfo" />

    <div *ngIf="nodeChangeInfo.initialTags" class="kpn-detail">
      <kpn-tags-table [tags]="initialTags" />
    </div>

    <div *ngIf="nodeChangeInfo.initialLatLon" class="kpn-detail">
      <kpn-node-moved-map [nodeMoved]="nodeMoved" />
    </div>
  `,
})
export class NodeChangeDetailComponent implements OnInit {
  @Input() nodeChangeInfo: NodeChangeInfo;
  initialTags: InterpretedTags;
  nodeMoved: NodeMoved;

  ngOnInit(): void {
    this.initialTags = InterpretedTags.nodeTags(
      this.nodeChangeInfo.initialTags
    );
    if (this.nodeChangeInfo.initialLatLon) {
      this.nodeMoved = {
        before: this.nodeChangeInfo.initialLatLon,
        after: this.nodeChangeInfo.initialLatLon,
        distance: 0,
      };
    }
  }

  hasTagDiffs(): boolean {
    return Util.hasTagDiffs(this.nodeChangeInfo.tagDiffs);
  }
}
