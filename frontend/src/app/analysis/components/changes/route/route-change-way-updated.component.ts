import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { WayUpdate } from '@api/common/diff/way-update';
import { NodeListComponent } from '@app/shared/components/link/node-list.component';
import { OsmLinkWayComponent } from '@app/shared/components/link/osm-link-way.component';
import { MetaDataComponent } from '@app/shared/components/meta-data.component';
import { Util } from '@app/shared/components/util';
import { TagDiffsComponent } from '../tag-diffs.component';

@Component({
  selector: 'ui-route-change-way-updated',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- eslint-disable @angular-eslint/template/cyclomatic-complexity -->
    <div class="kpn-level-4">
      <div class="kpn-level-4-header">
        <span class="kpn-label" i18n="@@route-change.way-update.title">Updated way</span>
        <ui-osm-link-way [wayId]="wayUpdate().id" [title]="wayUpdate().id.toString()" />
      </div>

      <div class="kpn-level-4-body">
        <div class="kpn-detail">
          <div class="kpn-thin">
            @if (isNewVersion(wayUpdate())) {
              <span i18n="@@route-change.way-update.changed-to">
                Changed to v{{ wayUpdate().after.version }}
              </span>
            } @else {
              <span i18n="@@route-change.way-update.version-unchanged">
                Way version unchanged
              </span>
            }
            <ui-meta-data [metaData]="wayUpdate().before" />
          </div>
        </div>

        @if (wayUpdate().directionReversed) {
          <div class="kpn-detail" i18n="@@route-change.way-update.direction-reversed">
            Direction reversed
          </div>
        }

        @if (wayUpdate().removedNodeIds.length > 0) {
          <div class="kpn-detail">
            <span class="kpn-label" i18n="@@route-change.way-update.removed-nodes">
              Removed node(s)
            </span>
            <ui-node-list [nodeIds]="wayUpdate().removedNodeIds" />
          </div>
        }

        @if (wayUpdate().addedNodeIds.length > 0) {
          <div class="kpn-detail">
            <span class="kpn-label" i18n="@@route-change.way-update.added-nodes"
              >Added node(s)</span
            >
            <ui-node-list [nodeIds]="wayUpdate().addedNodeIds" />
          </div>
        }

        @if (hasTagDiffs()) {
          <div class="kpn-detail">
            <ui-tag-diffs [tagDiffs]="wayUpdate().tagDiffs" />
          </div>
        }
      </div>
    </div>
  `,
  imports: [MetaDataComponent, NodeListComponent, OsmLinkWayComponent, TagDiffsComponent],
})
export class RouteChangeWayUpdatedComponent {
  readonly wayUpdate = input.required<WayUpdate>();

  isNewVersion(wayUpdate: WayUpdate): boolean {
    return wayUpdate.before.version !== wayUpdate.after.version;
  }

  hasTagDiffs(): boolean {
    return Util.hasTagDiffs(this.wayUpdate().tagDiffs);
  }
}
