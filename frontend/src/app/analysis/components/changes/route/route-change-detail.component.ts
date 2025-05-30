import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteChangeInfo } from '@api/common/route/route-change-info';
import { RouteChangeMapComponent } from './route-change-map.component';
import { RouteChangeWayAddedComponent } from './route-change-way-added.component';
import { RouteChangeWayRemovedComponent } from './route-change-way-removed.component';
import { RouteChangeWayUpdatedComponent } from './route-change-way-updated.component';
import { RouteDiffComponent } from './route-diff.component';

@Component({
  selector: 'ui-route-change-detail',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-route-diff [diffs]="routeChangeInfo().diffs" />

    @if (!routeChangeInfo().geometryDiff) {
      <div class="kpn-detail" i18n="@@route-change.no-geometry-diff">No geometry change</div>
    } @else {
      <div class="kpn-detail">
        <ui-route-change-map
          [geometryDiff]="routeChangeInfo().geometryDiff"
          [nodeChanges]="routeChangeInfo().nodeChanges"
          [bounds]="routeChangeInfo().bounds"
        />
      </div>
    }

    @for (removedWayInfo of routeChangeInfo().wayDiffs.removed; track $index) {
      <ui-route-change-way-removed [wayInfo]="removedWayInfo" />
    }

    @for (addedWayInfo of routeChangeInfo().wayDiffs.added; track $index) {
      <ui-route-change-way-added [routeChangeInfo]="routeChangeInfo()" [wayInfo]="addedWayInfo" />
    }

    @for (wayUpdate of routeChangeInfo().wayDiffs.updated; track $index) {
      <ui-route-change-way-updated [wayUpdate]="wayUpdate" />
    }
  `,
  imports: [
    RouteChangeMapComponent,
    RouteChangeWayAddedComponent,
    RouteChangeWayRemovedComponent,
    RouteChangeWayUpdatedComponent,
    RouteDiffComponent,
  ],
})
export class RouteChangeDetailComponent {
  routeChangeInfo = input.required<RouteChangeInfo>();
}
