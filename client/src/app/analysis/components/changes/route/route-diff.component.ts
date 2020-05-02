import {ChangeDetectionStrategy} from "@angular/core";
import {Component, Input} from "@angular/core";
import {RouteDiff} from "../../../../kpn/api/common/diff/route/route-diff";

@Component({
  selector: "kpn-route-diff",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <!-- route name -->
    <div *ngIf="!!diffs.nameDiff" class="kpn-detail" i18n="@@route-changes.route-diff.name-diff">
      Route name changed from "{{diffs.nameDiff.before}}" to "{{diffs.nameDiff.after}}".
    </div>

    <!-- route role -->
    <div *ngIf="isRouteRoleAdded()" class="kpn-detail" i18n="@@route-changes.route-diff.role-added">
      Route role "{{diffs.roleDiff.after}}" added to networkrelation.
    </div>

    <div *ngIf="isRouteRoleDeleted()" class="kpn-detail" i18n="@@route-changes.route-diff.role-deleted">
      Route role "{{diffs.roleDiff.before}}" removed from networkrelation.
    </div>

    <div *ngIf="isRouteRoleUpdated()" class="kpn-detail" i18n="@@route-changes.route-diff.role-updated">
      Route role in networkrelation changed from "{{diffs.roleDiff.before}}" to "{{diffs.roleDiff.after}}".
    </div>

    <!-- facts -->
    <kpn-fact-diffs [factDiffs]="diffs.factDiffs"></kpn-fact-diffs>

    <!-- nodeDiffs -->
    <ng-container *ngFor="let nodeDiff of diffs.nodeDiffs">
      <div *ngIf="!nodeDiff.added.isEmpty()" class="kpn-detail">
        <kpn-route-node-diff [title]="nodeDiff.title" action="added" [nodeRefs]="nodeDiff.added"></kpn-route-node-diff>
      </div>
      <div *ngIf="!nodeDiff.removed.isEmpty()" class="kpn-detail">
        <kpn-route-node-diff [title]="nodeDiff.title" action="removed" [nodeRefs]="nodeDiff.removed"></kpn-route-node-diff>
      </div>
    </ng-container>

    <!-- memberOrderChanged -->
    <div *ngIf="diffs.memberOrderChanged" class="kpn-detail" i18n="@@route-changes.route-diff.member-order-changed">
      Member order changed.
    </div>

    <!-- tagDiffs -->
    <kpn-tag-diffs [tagDiffs]="diffs.tagDiffs" class="kpn-detail"></kpn-tag-diffs>
  `
})
export class RouteDiffComponent {

  @Input() diffs: RouteDiff;

  isRouteRoleAdded(): boolean {
    return this.diffs.roleDiff && !this.diffs.roleDiff.before && !!this.diffs.roleDiff.after;
  }

  isRouteRoleDeleted(): boolean {
    return this.diffs.roleDiff && !!this.diffs.roleDiff.before && !this.diffs.roleDiff.after;
  }

  isRouteRoleUpdated(): boolean {
    return this.diffs.roleDiff && !!this.diffs.roleDiff.before && !!this.diffs.roleDiff.after;
  }

}

