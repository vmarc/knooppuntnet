import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteDiff } from '@api/common/diff/route';
import { Util } from '@app/components/shared';
import { FactDiffsComponent } from '../fact-diffs.component';
import { TagDiffsComponent } from '../tag-diffs.component';
import { RouteNodeDiffComponent } from './route-node-diff.component';

@Component({
  selector: 'kpn-route-diff',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    @if (diffs().nameDiff) {
      <div class="kpn-detail" i18n="@@route-changes.route-diff.name-diff">
        Route name changed from "{{ diffs().nameDiff.before }}" to "{{ diffs().nameDiff.after }}".
      </div>
    }

    @if (isRouteRoleAdded()) {
      <div class="kpn-detail" i18n="@@route-changes.route-diff.role-added">
        Route role "{{ diffs().roleDiff.after }}" added to networkrelation.
      </div>
    }

    @if (isRouteRoleDeleted()) {
      <div class="kpn-detail" i18n="@@route-changes.route-diff.role-deleted">
        Route role "{{ diffs().roleDiff.before }}" removed from networkrelation.
      </div>
    }

    @if (isRouteRoleUpdated()) {
      <div class="kpn-detail" i18n="@@route-changes.route-diff.role-updated">
        Route role in networkrelation changed from "{{ diffs().roleDiff.before }}" to "{{
          diffs().roleDiff.after
        }}".
      </div>
    }
    <kpn-fact-diffs [factDiffs]="diffs().factDiffs" />

    @for (nodeDiff of diffs().nodeDiffs; track $index) {
      @if (nodeDiff.added.length > 0) {
        <div class="kpn-detail">
          <kpn-route-node-diff
            action="added"
            [title]="nodeDiff.title"
            [nodeRefs]="nodeDiff.added"
          />
        </div>
      }
      @if (nodeDiff.removed.length > 0) {
        <div class="kpn-detail">
          <kpn-route-node-diff
            action="removed"
            [title]="nodeDiff.title"
            [nodeRefs]="nodeDiff.removed"
          />
        </div>
      }
    }

    @if (diffs().memberOrderChanged) {
      <div class="kpn-detail" i18n="@@route-changes.route-diff.member-order-changed">
        Member order changed.
      </div>
    }

    @if (hasTagDiffs()) {
      <div>
        <kpn-tag-diffs [tagDiffs]="diffs().tagDiffs" class="kpn-detail" />
      </div>
    }
  `,
  standalone: true,
  imports: [FactDiffsComponent, RouteNodeDiffComponent, TagDiffsComponent],
})
export class RouteDiffComponent {
  diffs = input.required<RouteDiff>();

  isRouteRoleAdded(): boolean {
    return this.diffs().roleDiff && !this.diffs().roleDiff.before && !!this.diffs().roleDiff.after;
  }

  isRouteRoleDeleted(): boolean {
    return this.diffs().roleDiff && !!this.diffs().roleDiff.before && !this.diffs().roleDiff.after;
  }

  isRouteRoleUpdated(): boolean {
    return this.diffs().roleDiff && !!this.diffs().roleDiff.before && !!this.diffs().roleDiff.after;
  }

  hasTagDiffs(): boolean {
    return Util.hasTagDiffs(this.diffs().tagDiffs);
  }
}
