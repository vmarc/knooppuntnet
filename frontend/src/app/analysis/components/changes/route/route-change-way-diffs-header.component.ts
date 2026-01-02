import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { WayDiffsInfo } from '@api/common/diff/way-diffs-info';

@Component({
  selector: 'ui-route-change-way-diffs-header',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let wayDiffs = wayDiffsInfo();

    <div class="kpn-line">
      <span class="kpn-label">Ways</span>
      <div class="kpn-comma-list">
        @if (wayDiffs.added.length > 0) {
          <span class="kpn-space-separated">
            <span>{{ wayDiffs.added.length }}</span>
            <span>added</span>
          </span>
        }
        @if (wayDiffs.removed.length > 0) {
          <span class="kpn-space-separated">
            <span>{{ wayDiffs.removed.length }}</span>
            <span>removed</span>
          </span>
        }
        @if (wayDiffs.updated.length > 0) {
          <span class="kpn-space-separated">
            <span>{{ wayDiffs.updated.length }}</span>
            <span>updated</span>
          </span>
        }
      </div>
    </div>
  `,
})
export class RouteChangeWayDiffsHeaderComponent {
  readonly wayDiffsInfo = input.required<WayDiffsInfo>();
}
