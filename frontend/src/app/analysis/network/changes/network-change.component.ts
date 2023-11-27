import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { NetworkChangeInfo } from '@api/common/changes/details';
import { ChangeType } from '@api/custom';
import { LinkNodeComponent } from '@app/components/shared/link';
import { LinkRouteComponent } from '@app/components/shared/link';

@Component({
  selector: 'kpn-network-change',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- changeType -->
    <div *ngIf="isCreate()">
      <b i18n="@@network-changes.network-created">Network created</b>
    </div>
    <div *ngIf="isDelete()">
      <b i18n="@@network-changes.network-deleted">Network deleted</b>
    </div>

    <div *ngIf="isInitialValue()">
      <span i18n="@@network-changes.network-initial-value">
        Oldest known state of the network.
      </span>
    </div>

    <!-- networkNodesAdded -->
    <div *ngIf="networkChangeInfo.networkNodes.added.length > 0" class="kpn-text-only-line">
      <span *ngIf="!isInitialValue()" class="kpn-label" i18n="@@network-changes.network-nodes.added"
        >Added node(s)</span
      >
      <span *ngIf="isInitialValue()" class="kpn-label" i18n="@@network-changes.network-nodes.list"
        >Nodes</span
      >
      <div class="kpn-comma-list">
        <span *ngFor="let ref of networkChangeInfo.networkNodes.added">
          <kpn-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
        </span>
      </div>
    </div>

    <!-- routesAdded -->
    <div *ngIf="networkChangeInfo.routes.added.length > 0" class="kpn-text-only-line">
      <span *ngIf="!isInitialValue()" class="kpn-label" i18n="@@network-changes.routes.added"
        >Added route(s)</span
      >
      <span *ngIf="isInitialValue()" class="kpn-label" i18n="@@network-changes.routes.list"
        >Routes</span
      >
      <div class="kpn-comma-list">
        <span *ngFor="let ref of networkChangeInfo.routes.added">
          <kpn-link-route
            [routeId]="ref.id"
            [routeName]="ref.name"
            [networkType]="networkChangeInfo.networkType"
          />
        </span>
      </div>
    </div>

    <!-- nodesAdded -->
    <div *ngIf="networkChangeInfo.nodes.added.length > 0">
      <span i18n="@@network-changes.nodes.added">
        Added non-network node member(s) in network relation
      </span>
    </div>

    <!-- waysAdded -->
    <div *ngIf="networkChangeInfo.ways.added.length > 0">
      <span i18n="@@network-changes.ways.added"> Added way member(s) in network relation </span>
    </div>

    <!-- relationsAdded -->
    <div *ngIf="networkChangeInfo.relations.added.length > 0">
      <span i18n="@@network-changes.relations.added">
        Added non-route relation(s) in network relation
      </span>
    </div>

    <!-- networkDataUpdate -->
    <div *ngIf="networkChangeInfo.networkDataUpdated">
      <span i18n="@@network-changes.network-relation-updated"> Updated network relation </span>
    </div>

    <!-- networkNodesUpdated -->
    <div *ngIf="networkChangeInfo.networkNodes.updated.length > 0" class="kpn-line">
      <span class="kpn-label" i18n="@@network-changes.network-nodes.updated">
        Updated network node(s)
      </span>
      <div class="kpn-comma-list">
        <span *ngFor="let ref of networkChangeInfo.networkNodes.updated">
          <kpn-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
        </span>
      </div>
    </div>

    <!-- routesUpdated -->
    <div *ngIf="networkChangeInfo.routes.updated.length > 0" class="kpn-line">
      <span class="kpn-label" i18n="@@network-changes.routes.updated"> Updated route(s) </span>
      <div class="kpn-comma-list">
        <span *ngFor="let ref of networkChangeInfo.routes.updated">
          <kpn-link-route
            [routeId]="ref.id"
            [routeName]="ref.name"
            [networkType]="networkChangeInfo.networkType"
          />
        </span>
      </div>
    </div>

    <!-- nodesUpdated -->
    <div *ngIf="networkChangeInfo.nodes.updated.length > 0">
      <span i18n="@@network-changes.nodes.updated"> Updated non-network node(s) </span>
    </div>

    <!-- waysUpdated -->
    <div *ngIf="networkChangeInfo.ways.updated.length > 0">
      <span i18n="@@network-changes.ways.updated">Updated way member(s)</span>
    </div>

    <!-- relationsUpdated -->
    <div *ngIf="networkChangeInfo.relations.updated.length > 0">
      <span i18n="@@network-changes.relations.updated"> Updated non-route relation(s) </span>
    </div>

    <!-- networkNodesRemoved -->
    <div *ngIf="networkChangeInfo.networkNodes.removed.length > 0" class="kpn-line">
      <span class="kpn-label" i18n="@@network-changes.network-nodes.removed">
        Removed network node(s)
      </span>
      <div class="kpn-comma-list">
        <span *ngFor="let ref of networkChangeInfo.networkNodes.removed">
          <kpn-link-node [nodeId]="ref.id" [nodeName]="ref.name" />
        </span>
      </div>
    </div>

    <!-- routesRemoved -->
    <div *ngIf="networkChangeInfo.routes.removed.length > 0" class="kpn-line">
      <span class="kpn-label" i18n="@@network-changes.routes.removed"> Removed route(s) </span>
      <div class="kpn-comma-list">
        <span *ngFor="let ref of networkChangeInfo.routes.removed">
          <kpn-link-route
            [routeId]="ref.id"
            [routeName]="ref.name"
            [networkType]="networkChangeInfo.networkType"
          />
        </span>
      </div>
    </div>

    <!-- nodesRemoved -->
    <div *ngIf="networkChangeInfo.nodes.removed.length > 0">
      <span i18n="@@network-changes.nodes.removed">
        Removed non-network node member(s) from network relation
      </span>
    </div>

    <!-- waysRemoved -->
    <div *ngIf="networkChangeInfo.ways.removed.length > 0">
      <span i18n="@@network-changes.ways.removed">
        Removed way member(s) from network relation
      </span>
    </div>

    <!-- relationsRemoved -->
    <div *ngIf="networkChangeInfo.relations.removed.length > 0">
      <span i18n="@@network-changes.relations.removed">
        Removed non-route relation(s) from network relation
      </span>
    </div>
  `,
  standalone: true,
  imports: [NgIf, NgFor, LinkNodeComponent, LinkRouteComponent],
})
export class NetworkChangeComponent {
  @Input() networkChangeInfo: NetworkChangeInfo;

  isCreate(): boolean {
    return this.isChangeType(ChangeType.create);
  }

  isUpdate(): boolean {
    return this.isChangeType(ChangeType.create);
  }

  isDelete(): boolean {
    return this.isChangeType(ChangeType.delete);
  }

  isInitialValue(): boolean {
    return this.isChangeType(ChangeType.initialValue);
  }

  private isChangeType(changeType: ChangeType): boolean {
    return this.networkChangeInfo.changeType === changeType;
  }
}
