import { NgIf } from '@angular/common';
import { NgFor } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { RouteChangeInfo } from '@api/common/route';
import { RouteChangeMapComponent } from './route-change-map.component';
import { RouteChangeWayAddedComponent } from './route-change-way-added.component';
import { RouteChangeWayRemovedComponent } from './route-change-way-removed.component';
import { RouteChangeWayUpdatedComponent } from './route-change-way-updated.component';
import { RouteDiffComponent } from './route-diff.component';

@Component({
  selector: 'kpn-route-change-detail',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-route-diff [diffs]="routeChangeInfo.diffs" />

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
  standalone: true,
  imports: [
    NgFor,
    NgIf,
    RouteChangeMapComponent,
    RouteChangeWayAddedComponent,
    RouteChangeWayRemovedComponent,
    RouteChangeWayUpdatedComponent,
    RouteDiffComponent,
  ],
})
export class RouteChangeDetailComponent {
  @Input() routeChangeInfo: RouteChangeInfo;
}
