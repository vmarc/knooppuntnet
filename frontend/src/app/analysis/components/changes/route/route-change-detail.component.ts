import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteChangeInfo } from '@api/common/route/route-change-info';
import { RouteChangeWayDiffsComponent } from './route-change-way-diffs.component';
import { RouteChangeGeometryDiffInfoComponent } from './route-change-geometry-diff-info.component';
import { RouteChangeMapComponent } from './route-change-map.component';
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
        <ui-route-change-geometry-diff-info [geometryDiff]="routeChangeInfo().geometryDiff" />
        <ui-route-change-map
          [geometryDiff]="routeChangeInfo().geometryDiff"
          [nodeChanges]="routeChangeInfo().nodeChanges"
          [bounds]="routeChangeInfo().bounds"
        />
      </div>
    }

    @if (routeChangeInfo()?.wayDiffs) {
      <ui-route-change-way-diffs [routeChangeInfo]="routeChangeInfo()" />
    }
  `,
  imports: [
    RouteChangeMapComponent,
    RouteDiffComponent,
    RouteChangeGeometryDiffInfoComponent,
    RouteChangeWayDiffsComponent,
  ],
})
export class RouteChangeDetailComponent {
  readonly routeChangeInfo = input.required<RouteChangeInfo>();
}
