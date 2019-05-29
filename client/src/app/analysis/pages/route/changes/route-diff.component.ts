import {Component, Input} from "@angular/core";
import {RouteDiff} from "../../../../kpn/shared/diff/route/route-diff";

@Component({
  selector: "kpn-route-diff",
  template: `

    <!-- route name -->
    <div *ngIf="!!diffs.nameDiff" class="kpn-detail">
      Route name changed from "{{diffs.nameDiff.before}}" to "{{diffs.nameDiff.after}}".
    </div>

    <!-- route role -->
    <div *ngIf="isRouteRoleAdded()" class="kpn-detail">
      Route role "{{diffs.roleDiff.after}}" added to networkrelation.
      <!-- Route rol "roleDiff.after" toegevoegd in netwerkrelatie. -->
    </div>

    <div *ngIf="isRouteRoleDeleted()" class="kpn-detail">
      Route role "{{diffs.roleDiff.before}}" removed from networkrelation.
      <!-- Route rol "roleDiff.before" verwijderd uit netwerkrelatie. -->
    </div>

    <div *ngIf="isRouteRoleUpdated()" class="kpn-detail">
      Route role in networkrelation changed from "{{diffs.roleDiff.before}}" to "{{diffs.roleDiff.after}}".
      <!-- Route rol in netwerkrelatie gewijzigd van "roleDiff.before" naar "roleDiff.after". -->
    </div>

    <!-- factDiffs factDiffs: FactDiffs -->
    <kpn-fact-diffs [factDiffs]="diffs.factDiffs"></kpn-fact-diffs>

    <!-- nodeDiffs nodeDiffs: List<RouteNodeDiff> -->


    <!-- memberOrderChanged memberOrderChanged: boolean-->


    <!-- tagDiffs tagDiffs: TagDiffs-->

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

