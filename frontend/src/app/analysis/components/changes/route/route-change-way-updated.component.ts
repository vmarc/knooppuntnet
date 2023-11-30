import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { RawNode } from '@api/common/data/raw';
import { NodeUpdate } from '@api/common/diff';
import { WayUpdate } from '@api/common/diff';
import { Util } from '@app/components/shared';
import { MetaDataComponent } from '@app/components/shared';
import { NodeListComponent } from '@app/components/shared/link';
import { OsmLinkWayComponent } from '@app/components/shared/link';
import { TagDiffsComponent } from '../tag-diffs.component';

@Component({
  selector: 'kpn-route-change-way-updated',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-level-4">
      <div class="kpn-level-4-header">
        <span class="kpn-label" i18n="@@route-change.way-update.title">Updated way</span>
        <kpn-osm-link-way [wayId]="wayUpdate.id" [title]="wayUpdate.id.toString()" />
      </div>

      <div class="kpn-level-4-body">
        <div class="kpn-detail">
          <div class="kpn-thin">
            @if (isNewVersion(wayUpdate)) {
              <span i18n="@@route-change.way-update.changed-to">
                Changed to v{{ wayUpdate.after.version }}
              </span>
            } @else {
              <span i18n="@@route-change.way-update.version-unchanged">
                Way version unchanged
              </span>
            }
            <kpn-meta-data [metaData]="wayUpdate.before" />
          </div>
        </div>

        @if (wayUpdate.directionReversed) {
          <div class="kpn-detail" i18n="@@route-change.way-update.direction-reversed">
            Direction reversed
          </div>
        }

        @if (wayUpdate.removedNodes.length > 0) {
          <div class="kpn-detail">
            <span class="kpn-label" i18n="@@route-change.way-update.removed-nodes">
              Removed node(s)
            </span>
            <kpn-node-list [nodeIds]="nodeIds(wayUpdate.removedNodes)" />
          </div>
        }

        @if (wayUpdate.addedNodes.length > 0) {
          <div class="kpn-detail">
            <span class="kpn-label" i18n="@@route-change.way-update.added-nodes"
              >Added node(s)</span
            >
            <kpn-node-list [nodeIds]="nodeIds(wayUpdate.addedNodes)" />
          </div>
        }

        @if (wayUpdate.updatedNodes.length > 0) {
          <div class="kpn-detail">
            <span class="kpn-label" i18n="@@route-change.way-update.updated-nodes">
              Updated node(s)
            </span>
            <kpn-node-list [nodeIds]="nodeUpdateIds(wayUpdate.updatedNodes)" />
          </div>
        }

        @if (hasTagDiffs()) {
          <div class="kpn-detail">
            <kpn-tag-diffs [tagDiffs]="wayUpdate.tagDiffs" />
          </div>
        }
      </div>
    </div>
  `,
  standalone: true,
  imports: [MetaDataComponent, NodeListComponent, OsmLinkWayComponent, TagDiffsComponent],
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
