import {Component, Input} from "@angular/core";
import {RouteChangeInfo} from "../../../../kpn/shared/route/route-change-info";

@Component({
  selector: "kpn-route-change-detail",
  template: `

    <kpn-route-diff [diffs]="routeChangeInfo.diffs"></kpn-route-diff>

    <div *ngIf="!routeChangeInfo.geometryDiff" class="kpn-detail" i18n="@@route-change.no-geometry-diff">
      No geometry change
    </div>

    <div *ngIf="routeChangeInfo.geometryDiff" class="kpn-detail">
      TODO MAP
      <!--
        UiSmallMap(
				  new RouteHistoryMap(
					  "map-" + key,
					  routeChangeInfo.nodes,
					  routeChangeInfo.bounds,
					  geometryDiff
				  )
				)
      -->
    </div>

    <kpn-route-change-way-removed
      *ngFor="let removedWayInfo of routeChangeInfo.removedWays"
      [wayInfo]="removedWayInfo">
    </kpn-route-change-way-removed>

    <kpn-route-change-way-added
      *ngFor="let addedWayInfo of routeChangeInfo.addedWays"
      [routeChangeInfo]="routeChangeInfo"
      [wayInfo]="addedWayInfo">
    </kpn-route-change-way-added>

    <kpn-route-change-way-updated
      *ngFor="let wayUpdate of routeChangeInfo.updatedWays"
      [wayUpdate]="wayUpdate">
    </kpn-route-change-way-updated>
  `
})
export class RouteChangeDetailComponent {
  @Input() routeChangeInfo: RouteChangeInfo;
}
