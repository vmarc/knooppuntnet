import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {NetworkChangeInfo} from "../../../kpn/api/common/changes/details/network-change-info";

@Component({
  selector: "kpn-network-change",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <!-- changeType -->
    <div *ngIf="networkChangeInfo.changeType.name === 'Create'">
      <b i18n="@@network-changes.network-created">
        Network created
      </b>
    </div>
    <div *ngIf="networkChangeInfo.changeType.name === 'Delete'">
      <b i18n="@@network-changes.network-deleted">
        Network deleted
      </b>
    </div>

    <div *ngIf="networkChangeInfo.changeType.name === 'InitialValue'">
      <p i18n="@@network-changes.network-initial-value">
        Oldest known state of network (at date of
        <a class="external" href="https://wiki.openstreetmap.org/wiki/NL:Open_Database_License" target="_blank" rel="nofollow noreferrer">license change</a>).
      </p>
    </div>

    <!-- networkNodesAdded -->
    <div *ngIf="!networkChangeInfo.networkNodes.added.isEmpty()">
      <p class="kpn-label" i18n="@@network-changes.network-nodes.added">Added node(s)</p>
      <div class="kpn-comma-list">
        <span *ngFor="let ref of networkChangeInfo.networkNodes.added">
          <kpn-link-node [nodeId]="ref.id" [nodeName]="ref.name"></kpn-link-node>
        </span>
      </div>
    </div>

    <!-- routesAdded -->
    <div *ngIf="!networkChangeInfo.routes.added.isEmpty()">
      <p class="kpn-label" i18n="@@network-changes.routes.added">Added route(s)</p>
      <div class="kpn-comma-list">
        <span *ngFor="let ref of networkChangeInfo.routes.added">
          <kpn-link-route [routeId]="ref.id" [title]="ref.name"></kpn-link-route>
        </span>
      </div>
    </div>

    <!-- nodesAdded -->
    <div *ngIf="!networkChangeInfo.nodes.added.isEmpty()">
      <span i18n="@@network-changes.nodes.added">
        Added non-network node member(s) in network relation
      </span>
    </div>

    <!-- waysAdded -->
    <div *ngIf="!networkChangeInfo.ways.added.isEmpty()">
      <span i18n="@@network-changes.ways.added">
        Added way member(s) in network relation
      </span>
    </div>

    <!-- relationsAdded -->
    <div *ngIf="!networkChangeInfo.relations.added.isEmpty()">
      <span i18n="@@network-changes.relations.added">
        Added non-route relation(s) in network relation
      </span>
    </div>

    <!-- networkDataUpdate -->
    <div *ngIf="networkChangeInfo.networkDataUpdated">
      <span i18n="@@network-changes.network-relation-updated">
        Updated network relation
      </span>
    </div>

    <!-- networkNodesUpdated -->
    <div *ngIf="!networkChangeInfo.networkNodes.updated.isEmpty()">
      <p class="kpn-label" i18n="@@network-changes.network-nodes.updated">
        Updated network node(s)
      </p>
      <div class="kpn-comma-list">
        <span *ngFor="let ref of networkChangeInfo.networkNodes.updated">
          <kpn-link-node [nodeId]="ref.id" [nodeName]="ref.name"></kpn-link-node>
        </span>
      </div>
    </div>

    <!-- routesUpdated -->
    <div *ngIf="!networkChangeInfo.routes.updated.isEmpty()">
      <p class="kpn-label" i18n="@@network-changes.routes.updated">
        Updated route(s)
      </p>
      <div class="kpn-comma-list">
        <span *ngFor="let ref of networkChangeInfo.routes.updated">
          <kpn-link-route [routeId]="ref.id" [title]="ref.name"></kpn-link-route>
        </span>
      </div>
    </div>

    <!-- nodesUpdated -->
    <div *ngIf="!networkChangeInfo.nodes.updated.isEmpty()">
      <span i18n="@@network-changes.nodes.updated">
        Updated non-network node(s)
      </span>
    </div>

    <!-- waysUpdated -->
    <div *ngIf="!networkChangeInfo.ways.updated.isEmpty()">
      <span i18n="@@network-changes.ways.updated">
        Updated way member(s)
      </span>
    </div>

    <!-- relationsUpdated -->
    <div *ngIf="!networkChangeInfo.relations.updated.isEmpty()">
      <span i18n="@@network-changes.relations.updated">
        Updated non-route relation(s)
      </span>
    </div>

    <!-- networkNodesRemoved -->
    <div *ngIf="!networkChangeInfo.networkNodes.removed.isEmpty()">
      <p class="kpn-label" i18n="@@network-changes.network-nodes.removed">
        Removed network node(s)
      </p>
      <div class="kpn-comma-list">
        <span *ngFor="let ref of networkChangeInfo.networkNodes.removed">
          <kpn-link-node [nodeId]="ref.id" [nodeName]="ref.name"></kpn-link-node>
        </span>
      </div>
    </div>

    <!-- routesRemoved -->
    <div *ngIf="!networkChangeInfo.routes.removed.isEmpty()">
      <p class="kpn-label" i18n="@@network-changes.routes.removed">
        Removed route(s)
      </p>
      <div class="kpn-comma-list">
        <span *ngFor="let ref of networkChangeInfo.routes.removed">
          <kpn-link-route [routeId]="ref.id" [title]="ref.name"></kpn-link-route>
        </span>
      </div>
    </div>

    <!-- nodesRemoved -->
    <div *ngIf="!networkChangeInfo.nodes.removed.isEmpty()">
      <span i18n="@@network-changes.nodes.removed">
        Removed non-network node member(s) from network relation
      </span>
    </div>

    <!-- waysRemoved -->
    <div *ngIf="!networkChangeInfo.ways.removed.isEmpty()">
      <span i18n="@@network-changes.ways.removed">
        Removed way member(s) from network relation
      </span>
    </div>

    <!-- relationsRemoved -->
    <div *ngIf="!networkChangeInfo.relations.removed.isEmpty()">
      <span i18n="@@network-changes.relations.removed">
        Removed non-route relation(s) from network relation
      </span>
    </div>
  `
})
export class NetworkChangeComponent {
  @Input() networkChangeInfo: NetworkChangeInfo;
}
