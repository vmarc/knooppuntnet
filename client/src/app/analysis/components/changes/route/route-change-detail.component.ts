import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { RouteChangeInfo } from '@api/common/route/route-change-info';

@Component({
  selector: 'kpn-route-change-detail',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-route-diff [diffs]="routeChangeInfo.diffs"/>

    <div
      *ngIf="!routeChangeInfo.geometryDiff"
      class="kpn-detail"
      i18n="@@route-change.no-geometry-diff"
    >
      No geometry change
    </div>

    <div *ngIf="routeChangeInfo.geometryDiff" class="kpn-detail">
      <kpn-route-change-map
        [geometryDiff]="routeChangeInfo.geometryDiff"
        [nodes]="routeChangeInfo.nodes"
        [bounds]="routeChangeInfo.bounds"
      />
    </div>

    <kpn-route-change-way-removed
      *ngFor="let removedWayInfo of routeChangeInfo.removedWays"
      [wayInfo]="removedWayInfo"
    />

    <kpn-route-change-way-added
      *ngFor="let addedWayInfo of routeChangeInfo.addedWays"
      [routeChangeInfo]="routeChangeInfo"
      [wayInfo]="addedWayInfo"
    />

    <kpn-route-change-way-updated
      *ngFor="let wayUpdate of routeChangeInfo.updatedWays"
      [wayUpdate]="wayUpdate"
    />
  `,
})
export class RouteChangeDetailComponent {
  @Input() routeChangeInfo: RouteChangeInfo;
}
