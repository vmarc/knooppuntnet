import { computed } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details/network-change-info';
import { LinkNodeComponent } from '@app/shared/components/link/link-node.component';
import { LinkRouteComponent } from '@app/shared/components/link/link-route.component';

@Component({
  selector: 'ui-network-change',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- changeType -->
    @if (changeType() === 'create') {
      <div>
        <b i18n="@@network-changes.network-created">Network created</b>
      </div>
    }
    @if (changeType() === 'delete') {
      <div>
        <b i18n="@@network-changes.network-deleted">Network deleted</b>
      </div>
    }

    @if (changeType() === 'initial-value') {
      <div i18n="@@network-changes.network-initial-value">Oldest known state of the network.</div>
    }

    <!-- networkNodesAdded -->
    @if (networkChangeInfo().networkNodes.added.length > 0) {
      <div class="kpn-text-only-line">
        @if (changeType() === 'initial-value') {
          <span class="kpn-label" i18n="@@network-changes.network-nodes.list">Nodes</span>
        } @else {
          <span class="kpn-label" i18n="@@network-changes.network-nodes.added">Added node(s)</span>
        }
        <div class="kpn-comma-list">
          @for (ref of networkChangeInfo().networkNodes.added; track $index) {
            <ui-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
          }
        </div>
      </div>
    }

    <!-- routesAdded -->
    @if (networkChangeInfo().routes.added.length > 0) {
      <div class="kpn-text-only-line">
        @if (changeType() === 'initial-value') {
          <span class="kpn-label" i18n="@@network-changes.routes.list">Routes</span>
        } @else {
          <span class="kpn-label" i18n="@@network-changes.routes.added">Added route(s)</span>
        }
        <div class="kpn-comma-list">
          @for (ref of networkChangeInfo().routes.added; track $index) {
            <ui-link-route
              [routeId]="ref.id"
              [routeName]="ref.name"
              [routeType]="networkChangeInfo().routeType"
            />
          }
        </div>
      </div>
    }

    <!-- nodesAdded -->
    @if (networkChangeInfo().nodes.added.length > 0) {
      <div i18n="@@network-changes.nodes.added">
        Added non-network node member(s) in network relation
      </div>
    }

    <!-- waysAdded -->
    @if (networkChangeInfo().ways.added.length > 0) {
      <div i18n="@@network-changes.ways.added">Added way member(s) in network relation</div>
    }

    <!-- relationsAdded -->
    @if (networkChangeInfo().relations.added.length > 0) {
      <div i18n="@@network-changes.relations.added">
        Added non-route relation(s) in network relation
      </div>
    }

    <!-- networkDataUpdate -->
    @if (networkChangeInfo().networkDataUpdated) {
      <div i18n="@@network-changes.network-relation-updated">Updated network relation</div>
    }

    <!-- networkNodesUpdated -->
    @if (networkChangeInfo().networkNodes.updated.length > 0) {
      <div class="kpn-line">
        <span class="kpn-label" i18n="@@network-changes.network-nodes.updated">
          Updated network node(s)
        </span>
        <div class="kpn-comma-list">
          @for (ref of networkChangeInfo().networkNodes.updated; track $index) {
            <ui-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
          }
        </div>
      </div>
    }

    <!-- routesUpdated -->
    @if (networkChangeInfo().routes.updated.length > 0) {
      <div class="kpn-line">
        <span class="kpn-label" i18n="@@network-changes.routes.updated">Updated route(s)</span>
        <div class="kpn-comma-list">
          @for (ref of networkChangeInfo().routes.updated; track $index) {
            <ui-link-route
              [routeId]="ref.id"
              [routeName]="ref.name"
              [routeType]="networkChangeInfo().routeType"
            />
          }
        </div>
      </div>
    }

    <!-- nodesUpdated -->
    @if (networkChangeInfo().nodes.updated.length > 0) {
      <div i18n="@@network-changes.nodes.updated">Updated non-network node(s)</div>
    }

    <!-- waysUpdated -->
    @if (networkChangeInfo().ways.updated.length > 0) {
      <div i18n="@@network-changes.ways.updated">Updated way member(s)</div>
    }

    <!-- relationsUpdated -->
    @if (networkChangeInfo().relations.updated.length > 0) {
      <div i18n="@@network-changes.relations.updated">Updated non-route relation(s)</div>
    }

    <!-- networkNodesRemoved -->
    @if (networkChangeInfo().networkNodes.removed.length > 0) {
      <div class="kpn-line">
        <span class="kpn-label" i18n="@@network-changes.network-nodes.removed">
          Removed network node(s)
        </span>
        <div class="kpn-comma-list">
          @for (ref of networkChangeInfo().networkNodes.removed; track $index) {
            <ui-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
          }
        </div>
      </div>
    }

    <!-- routesRemoved -->
    @if (networkChangeInfo().routes.removed.length > 0) {
      <div class="kpn-line">
        <span class="kpn-label" i18n="@@network-changes.routes.removed">Removed route(s)</span>
        <div class="kpn-comma-list">
          @for (ref of networkChangeInfo().routes.removed; track $index) {
            <ui-link-route
              [routeId]="ref.id"
              [routeName]="ref.name"
              [routeType]="networkChangeInfo().routeType"
            />
          }
        </div>
      </div>
    }

    <!-- nodesRemoved -->
    @if (networkChangeInfo().nodes.removed.length > 0) {
      <div i18n="@@network-changes.nodes.removed">
        Removed non-network node member(s) from network relation
      </div>
    }

    <!-- waysRemoved -->
    @if (networkChangeInfo().ways.removed.length > 0) {
      <div i18n="@@network-changes.ways.removed">Removed way member(s) from network relation</div>
    }

    <!-- relationsRemoved -->
    @if (networkChangeInfo().relations.removed.length > 0) {
      <div i18n="@@network-changes.relations.removed">
        Removed non-route relation(s) from network relation
      </div>
    }
  `,
  imports: [LinkNodeComponent, LinkRouteComponent],
})
export class NetworkChangeComponent {
  readonly networkChangeInfo = input.required<NetworkChangeInfo>();
  readonly changeType = computed(() => this.networkChangeInfo().changeType);
}
