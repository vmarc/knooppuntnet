import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {RawNode} from "../../../../kpn/api/common/data/raw/raw-node";
import {NodeUpdate} from "../../../../kpn/api/common/diff/node-update";
import {WayUpdate} from "../../../../kpn/api/common/diff/way-update";

@Component({
  selector: "kpn-route-change-way-updated",
  template: `
    <div class="kpn-level-4">
      <div class="kpn-level-4-header">
        <span i18n="@@route-change.way-update.title">Updated way</span>
        <kpn-osm-link-way [wayId]="wayUpdate.id" [title]="wayUpdate.id.toString()"></kpn-osm-link-way>
      </div>

      <div class="kpn-level-4-body">
        <div class="kpn-detail">
          <div class="kpn-thin">
            <ng-container *ngIf="isNewVersion(wayUpdate)">
              <ng-container i18n="@@route-change.way-update.changed-to">Changed to v{{wayUpdate.after.version}}.</ng-container>
            </ng-container>
            <ng-container *ngIf="!isNewVersion(wayUpdate)" i18n="@@route-change.way-update.version-unchanged">
              Way version unchanged
            </ng-container>
            <kpn-meta-data [metaData]="wayUpdate.after"></kpn-meta-data>
          </div>
        </div>

        <div *ngIf="wayUpdate.directionReversed" class="kpn-detail" i18n="@@route-change.way-update.direction-reversed">
          Direction reversed
        </div>

        <div *ngIf="!wayUpdate.removedNodes.isEmpty()" class="kpn-detail">
          <span i18n="@@route-change.way-update.removed-nodes">Removed node(s)</span>:
          <kpn-node-list [nodeIds]="nodeIds(wayUpdate.removedNodes)"></kpn-node-list>
        </div>

        <div *ngIf="!wayUpdate.addedNodes.isEmpty()" class="kpn-detail">
          <span class="kpn-label" i18n="@@route-change.way-update.added-nodes">Added node(s)</span>
          <kpn-node-list [nodeIds]="nodeIds(wayUpdate.addedNodes)"></kpn-node-list>
        </div>

        <div *ngIf="!wayUpdate.updatedNodes.isEmpty()" class="kpn-detail">
          <span class="kpn-label" i18n="@@route-change.way-update.updated-nodes">Updated node(s)</span>
          <kpn-node-list [nodeIds]="nodeUpdateIds(wayUpdate.updatedNodes)"></kpn-node-list>
        </div>

        <div class="kpn-detail">
          <kpn-tag-diffs [tagDiffs]="wayUpdate.tagDiffs"></kpn-tag-diffs>
        </div>
      </div>
    </div>
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
    return wayUpdate.before.version !== wayUpdate.after.version;
  }

}
