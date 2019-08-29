import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {RawNode} from "../../../kpn/shared/data/raw/raw-node";
import {NodeUpdate} from "../../../kpn/shared/diff/node-update";
import {WayUpdate} from "../../../kpn/shared/diff/way-update";

@Component({
  selector: "kpn-route-change-way-updated",
  template: `
    <div>
      <span i18n="@@route-change.way-update.title">Updated way</span>
      <kpn-osm-link-way [wayId]="wayUpdate.id" [title]="wayUpdate.id.toString()"></kpn-osm-link-way>
    </div>

    <div class="kpn-detail">
      <div class="kpn-thin">
        <div *ngIf="isNewVersion(wayUpdate)">
          <ng-container i18n="@@route-change.way-update.changed-to">Changed to</ng-container>
          v{{wayUpdate.after.version}}.
        </div>
        <div *ngIf="!isNewVersion(wayUpdate)" i18n="@@route-change.way-update.version-unchanged">
          Way version unchanged
        </div>
        <kpn-meta-data [metaData]="wayUpdate.after"></kpn-meta-data>
      </div>
    </div>

    <div *ngIf="!wayUpdate.removedNodes.isEmpty()">
      <span i18n="@@route-change.way-update.removed-nodes">Removed node(s)</span>
      <div *ngFor="let removedNodeId of nodeIds(wayUpdate.removedNodes)" class="kpn-comma-list">
        <kpn-osm-link-node [nodeId]="removedNodeId" [title]="removedNodeId.toString()"></kpn-osm-link-node>
      </div>
    </div>

    <div *ngIf="!wayUpdate.addedNodes.isEmpty()">
      <span i18n="@@route-change.way-update.added-nodes">Added node(s)</span>
      <div *ngFor="let addedNodeId of nodeIds(wayUpdate.addedNodes)" class="kpn-comma-list">
        <kpn-osm-link-node [nodeId]="addedNodeId" [title]="addedNodeId.toString()"></kpn-osm-link-node>
      </div>
    </div>

    <div *ngIf="!wayUpdate.updatedNodes.isEmpty()">
      <span i18n="@@route-change.way-update.updated-nodes">Updated node(s)</span>
      <div *ngFor="let updatedNodeId of nodeUpdateIds(wayUpdate.updatedNodes)" class="kpn-comma-list">
        <kpn-osm-link-node [nodeId]="updatedNodeId" [title]="updatedNodeId.toString()"></kpn-osm-link-node>
      </div>
    </div>

    <kpn-tag-diffs [tagDiffs]="wayUpdate.tagDiffs"></kpn-tag-diffs>
  `
})
export class RouteChangeWayUpdatedComponent {

  @Input() wayUpdate: WayUpdate;

  nodeIds(nodes: List<RawNode>): List<number> {
    return nodes.map(node => node.id);
  }

  nodeUpdateIds(nodes: List<NodeUpdate>): List<number> {
    return nodes.map(node => node.after.id);
  }

  isNewVersion(wayUpdate: WayUpdate): boolean {
    return wayUpdate.before.version != wayUpdate.after.version;
  }

}
