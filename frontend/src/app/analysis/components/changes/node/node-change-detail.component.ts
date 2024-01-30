import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NodeMoved } from '@api/common/diff/node';
import { NodeChangeInfo } from '@api/common/node';
import { FactNameComponent } from '@app/analysis/fact';
import { Util } from '@app/components/shared';
import { LinkNetworkDetailsComponent } from '@app/components/shared/link';
import { LinkRouteComponent } from '@app/components/shared/link';
import { InterpretedTags } from '@app/components/shared/tags';
import { TagsTableComponent } from '@app/components/shared/tags';
import { FactDiffsComponent } from '../fact-diffs.component';
import { TagDiffsComponent } from '../tag-diffs.component';
import { NodeChangeMovedComponent } from './node-change-moved.component';
import { NodeMovedMapComponent } from './node-moved-map.component';

@Component({
  selector: 'kpn-node-change-detail',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @for (fact of nodeChangeInfo().facts; track $index) {
      <div class="kpn-detail">
        <kpn-fact-name [fact]="fact" />
      </div>
    }

    @for (change of nodeChangeInfo().connectionChanges; track $index) {
      <div class="kpn-detail">
        @if (change.after) {
          <span i18n="@@node-change.belongs-to-another-network" class="kpn-label">
            This node belongs to another network
          </span>
        } @else {
          <span i18n="@@node-change.no-longer-belongs-to-another-network" class="kpn-label">
            This node no longer belongs to another network
          </span>
        }
        <kpn-link-network-details [networkId]="change.ref.id" [networkName]="change.ref.name" />
      </div>
    }

    @for (change of nodeChangeInfo().roleConnectionChanges; track $index) {
      <div class="kpn-detail">
        @if (change.after) {
          <span i18n="@@node-change.received-role-connection-in-network-relation" class="kpn-label">
            This node received role "connection" in the network relation
          </span>
        } @else {
          <span i18n="@@node-change.lost-role-connection-in-network-relation" class="kpn-label">
            This node no longer has role "connection" in the network relation
          </span>
        }
        <kpn-link-network-details [networkId]="change.ref.id" [networkName]="change.ref.name" />
      </div>
    }

    @for (change of nodeChangeInfo().definedInNetworkChanges; track $index) {
      <div class="kpn-detail">
        @if (change.after) {
          <span i18n="@@node-change.added-to-network-relation" class="kpn-label">
            Added to network relation
          </span>
        } @else {
          <span i18n="@@node-change.removed-from-network-relation" class="kpn-label">
            Removed from network relation
          </span>
        }
        <kpn-link-network-details [networkId]="change.ref.id" [networkName]="change.ref.name" />
      </div>
    }

    @for (ref of nodeChangeInfo().addedToRoute; track $index) {
      <div class="kpn-detail">
        <span i18n="@@node-change.added-to-route" class="kpn-label">Added to route</span>
        <kpn-link-route [routeId]="ref.id" [routeName]="ref.name" />
      </div>
    }

    @for (ref of nodeChangeInfo().addedToNetwork; track $index) {
      <div class="kpn-detail">
        <span i18n="@@node-change.added-to-network" class="kpn-label">Added to network</span>
        <kpn-link-network-details [networkId]="ref.id" [networkName]="ref.name" />
      </div>
    }

    @for (ref of nodeChangeInfo().removedFromRoute; track $index) {
      <div class="kpn-detail">
        <span i18n="@@node-change.removed-from-route" class="kpn-label">Removed from route</span>
        <kpn-link-route [routeId]="ref.id" [routeName]="ref.name" />
      </div>
    }

    @for (ref of nodeChangeInfo().removedFromNetwork; track $index) {
      <div class="kpn-detail">
        <span i18n="@@node-change.removed-from-network" class="kpn-label">
          Removed from network
        </span>
        <kpn-link-network-details [networkId]="ref.id" [networkName]="ref.name" />
      </div>
    }
    <kpn-fact-diffs [factDiffs]="nodeChangeInfo().factDiffs" />

    @if (hasTagDiffs()) {
      <div class="kpn-detail">
        <kpn-tag-diffs [tagDiffs]="nodeChangeInfo().tagDiffs" />
      </div>
    }

    <kpn-node-change-moved [nodeChangeInfo]="nodeChangeInfo()" />

    @if (nodeChangeInfo().initialTags) {
      <div class="kpn-detail">
        <kpn-tags-table [tags]="initialTags" />
      </div>
    }

    @if (nodeChangeInfo().initialLatLon) {
      <div class="kpn-detail">
        <kpn-node-moved-map [nodeMoved]="nodeMoved" />
      </div>
    }
  `,
  standalone: true,
  imports: [
    FactDiffsComponent,
    FactNameComponent,
    LinkNetworkDetailsComponent,
    LinkRouteComponent,
    NodeChangeMovedComponent,
    NodeMovedMapComponent,
    TagDiffsComponent,
    TagsTableComponent,
  ],
})
export class NodeChangeDetailComponent implements OnInit {
  nodeChangeInfo = input.required<NodeChangeInfo>();
  initialTags: InterpretedTags;
  nodeMoved: NodeMoved;

  ngOnInit(): void {
    this.initialTags = InterpretedTags.nodeTags(this.nodeChangeInfo().initialTags);
    if (this.nodeChangeInfo().initialLatLon) {
      this.nodeMoved = {
        before: this.nodeChangeInfo().initialLatLon,
        after: this.nodeChangeInfo().initialLatLon,
        distance: 0,
      };
    }
  }

  hasTagDiffs(): boolean {
    return Util.hasTagDiffs(this.nodeChangeInfo().tagDiffs);
  }
}
