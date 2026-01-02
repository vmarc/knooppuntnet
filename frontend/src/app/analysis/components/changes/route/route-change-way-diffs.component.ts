import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteChangeInfo } from '@api/common/route/route-change-info';
import { RouteChangeWayDiffsHeaderComponent } from './route-change-way-diffs-header.component';
import { NzCollapsePanelComponent } from 'ng-zorro-antd/collapse';
import { NzCollapseComponent } from 'ng-zorro-antd/collapse';
import { RouteChangeWayAddedComponent } from './route-change-way-added.component';
import { RouteChangeWayRemovedComponent } from './route-change-way-removed.component';
import { RouteChangeWayUpdatedComponent } from './route-change-way-updated.component';

@Component({
  selector: 'ui-route-change-way-diffs',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let wayDiffs = routeChangeInfo().wayDiffs;

    <nz-collapse nzGhost>
      <nz-collapse-panel [nzHeader]="header">
        <ng-template #header>
          <ui-route-change-way-diffs-header [wayDiffsInfo]="wayDiffs" />
        </ng-template>
        @for (removedWayInfo of wayDiffs.removed; track $index) {
          <ui-route-change-way-removed [wayInfo]="removedWayInfo" />
        }
        @for (addedWayInfo of wayDiffs.added; track $index) {
          <ui-route-change-way-added
            [routeChangeInfo]="routeChangeInfo()"
            [wayInfo]="addedWayInfo"
          />
        }
        @for (wayUpdate of wayDiffs.updated; track $index) {
          <ui-route-change-way-updated [wayUpdate]="wayUpdate" />
        }
      </nz-collapse-panel>
    </nz-collapse>
  `,
  imports: [
    RouteChangeWayAddedComponent,
    RouteChangeWayRemovedComponent,
    RouteChangeWayUpdatedComponent,
    RouteChangeWayDiffsHeaderComponent,
    NzCollapseComponent,
    NzCollapsePanelComponent,
  ],
})
export class RouteChangeWayDiffsComponent {
  readonly routeChangeInfo = input.required<RouteChangeInfo>();
}
