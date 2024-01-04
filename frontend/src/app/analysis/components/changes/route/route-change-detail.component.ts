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

    @if (!routeChangeInfo.geometryDiff) {
      <div class="kpn-detail" i18n="@@route-change.no-geometry-diff">No geometry change</div>
    } @else {
      <div class="kpn-detail">
        <kpn-route-change-map
          [geometryDiff]="routeChangeInfo.geometryDiff"
          [nodes]="routeChangeInfo.nodes"
          [bounds]="routeChangeInfo.bounds"
        />
      </div>
    }

    @for (removedWayInfo of routeChangeInfo.removedWays; track $index) {
      <kpn-route-change-way-removed [wayInfo]="removedWayInfo" />
    }

    @for (addedWayInfo of routeChangeInfo.addedWays; track $index) {
      <kpn-route-change-way-added [routeChangeInfo]="routeChangeInfo" [wayInfo]="addedWayInfo" />
    }

    @for (wayUpdate of routeChangeInfo.updatedWays; track $index) {
      <kpn-route-change-way-updated [wayUpdate]="wayUpdate" />
    }
  `,
  standalone: true,
  imports: [
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
