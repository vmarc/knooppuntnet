import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { RouteDiff } from '@api/common/diff/route/route-diff';
import { Util } from '@app/components/shared/util';

@Component({
  selector: 'kpn-route-diff',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div
      *ngIf="!!diffs.nameDiff"
      class="kpn-detail"
      i18n="@@route-changes.route-diff.name-diff"
    >
      Route name changed from "{{ diffs.nameDiff.before }}" to "{{
        diffs.nameDiff.after
      }}".
    </div>

    <div
      *ngIf="isRouteRoleAdded()"
      class="kpn-detail"
      i18n="@@route-changes.route-diff.role-added"
    >
      Route role "{{ diffs.roleDiff.after }}" added to networkrelation.
    </div>

    <div
      *ngIf="isRouteRoleDeleted()"
      class="kpn-detail"
      i18n="@@route-changes.route-diff.role-deleted"
    >
      Route role "{{ diffs.roleDiff.before }}" removed from networkrelation.
    </div>

    <div
      *ngIf="isRouteRoleUpdated()"
      class="kpn-detail"
      i18n="@@route-changes.route-diff.role-updated"
    >
      Route role in networkrelation changed from "{{ diffs.roleDiff.before }}"
      to "{{ diffs.roleDiff.after }}".
    </div>

    <kpn-fact-diffs [factDiffs]="diffs.factDiffs" />

    <ng-container *ngFor="let nodeDiff of diffs.nodeDiffs">
      <div *ngIf="nodeDiff.added.length > 0" class="kpn-detail">
        <kpn-route-node-diff
          action="added"
          [title]="nodeDiff.title"
          [nodeRefs]="nodeDiff.added"
        />
      </div>
      <div *ngIf="nodeDiff.removed.length > 0" class="kpn-detail">
        <kpn-route-node-diff
          action="removed"
          [title]="nodeDiff.title"
          [nodeRefs]="nodeDiff.removed"
        />
      </div>
    </ng-container>

    <div
      *ngIf="diffs.memberOrderChanged"
      class="kpn-detail"
      i18n="@@route-changes.route-diff.member-order-changed"
    >
      Member order changed.
    </div>

    <div *ngIf="hasTagDiffs()">
      <kpn-tag-diffs [tagDiffs]="diffs.tagDiffs" class="kpn-detail" />
    </div>
  `,
})
export class RouteDiffComponent {
  @Input() diffs: RouteDiff;

  isRouteRoleAdded(): boolean {
    return (
      this.diffs.roleDiff &&
      !this.diffs.roleDiff.before &&
      !!this.diffs.roleDiff.after
    );
  }

  isRouteRoleDeleted(): boolean {
    return (
      this.diffs.roleDiff &&
      !!this.diffs.roleDiff.before &&
      !this.diffs.roleDiff.after
    );
  }

  isRouteRoleUpdated(): boolean {
    return (
      this.diffs.roleDiff &&
      !!this.diffs.roleDiff.before &&
      !!this.diffs.roleDiff.after
    );
  }

  hasTagDiffs(): boolean {
    return Util.hasTagDiffs(this.diffs.tagDiffs);
  }
}
