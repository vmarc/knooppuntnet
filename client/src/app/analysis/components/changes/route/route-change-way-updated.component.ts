import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { RawNode } from '@api/common/data/raw/raw-node';
import { NodeUpdate } from '@api/common/diff/node-update';
import { WayUpdate } from '@api/common/diff/way-update';
import { Util } from '../../../../components/shared/util';

@Component({
  selector: 'kpn-route-change-way-updated',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-level-4">
      <div class="kpn-level-4-header">
        <span class="kpn-label" i18n="@@route-change.way-update.title"
        >Updated way</span
        >
        <kpn-osm-link-way [wayId]="wayUpdate.id" [title]="wayUpdate.id.toString()" />
      </div>

      <div class="kpn-level-4-body">
        <div class="kpn-detail">
          <div class="kpn-thin">
            <ng-container
              *ngIf="isNewVersion(wayUpdate)"
              i18n="@@route-change.way-update.changed-to"
            >
              Changed to v{{ wayUpdate.after.version }}
            </ng-container>
            <ng-container
              *ngIf="!isNewVersion(wayUpdate)"
              i18n="@@route-change.way-update.version-unchanged"
            >
              Way version unchanged
            </ng-container>
            <kpn-meta-data [metaData]="wayUpdate.before" />
          </div>
        </div>

        <div
          *ngIf="wayUpdate.directionReversed"
          class="kpn-detail"
          i18n="@@route-change.way-update.direction-reversed"
        >
          Direction reversed
        </div>

        <div *ngIf="wayUpdate.removedNodes.length > 0" class="kpn-detail">
          <span class="kpn-label" i18n="@@route-change.way-update.removed-nodes"
          >Removed node(s)</span
          >
          <kpn-node-list [nodeIds]="nodeIds(wayUpdate.removedNodes)" />
        </div>

        <div *ngIf="wayUpdate.addedNodes.length > 0" class="kpn-detail">
          <span class="kpn-label" i18n="@@route-change.way-update.added-nodes"
          >Added node(s)</span
          >
          <kpn-node-list [nodeIds]="nodeIds(wayUpdate.addedNodes)" />
        </div>

        <div *ngIf="wayUpdate.updatedNodes.length > 0" class="kpn-detail">
          <span class="kpn-label" i18n="@@route-change.way-update.updated-nodes"
          >Updated node(s)</span
          >
          <kpn-node-list [nodeIds]="nodeUpdateIds(wayUpdate.updatedNodes)" />
        </div>

        <div *ngIf="hasTagDiffs()" class="kpn-detail">
          <kpn-tag-diffs [tagDiffs]="wayUpdate.tagDiffs"/>
        </div>
      </div>
    </div>
  `,
})
export class RouteChangeWayUpdatedComponent {
  @Input() wayUpdate: WayUpdate;

  nodeIds(nodes: RawNode[]): number[] {
    return nodes.map((node) => node.id);
  }

  nodeUpdateIds(nodes: NodeUpdate[]): number[] {
    return nodes.map((node) => node.after.id);
  }

  isNewVersion(wayUpdate: WayUpdate): boolean {
    return wayUpdate.before.version !== wayUpdate.after.version;
  }

  hasTagDiffs(): boolean {
    return Util.hasTagDiffs(this.wayUpdate.tagDiffs);
  }
}
