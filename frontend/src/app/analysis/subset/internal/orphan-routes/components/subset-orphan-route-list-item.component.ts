import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OrphanRouteInfo } from '@api/common/orphan-route-info';
import { RouteType } from '@api/common/route-type';
import { DayComponent } from '@app/shared/components/day/day.component';
import { DistancePipe } from '@app/shared/components/format/distance.pipe';
import { LinkRouteComponent } from '@app/shared/components/link/link-route.component';
import { ActionButtonRouteComponent } from '../../../../components/action/action-button-route.component';
import { SubsetOrphanRouteAnalysisComponent } from './subset-orphan-route-analysis.component';

@Component({
  selector: 'ui-subset-orphan-route-list-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    @let route = row();
    <div class="kpn-line">
      <span>{{ rowNumber() }}</span>
      <ui-action-button-route [routeType]="routeType()" [relationId]="route.id" />
      <ui-link-route [routeId]="route.id" [routeName]="route.name" [routeType]="routeType()" />
      <span>{{ route.meters | distance }}</span>
    </div>
    <div class="kpn-line">
      <ui-subset-orphan-route-analysis [route]="route" [routeType]="routeType()" />
      <span>
        <span i18n="@@subset-orphan-routes.table.last-edit" class="kpn-label">Last edit</span>
        <ui-day [timestamp]="route.lastUpdated" />
      </span>
    </div>
    @if (route.lastSurvey) {
      <div>
        <span i18n="@@subset-orphan-routes.table.last-survey" class="kpn-label">Survey</span>
        <span>
          {{ route.lastSurvey }}
        </span>
      </div>
    }
  `,
  imports: [
    ActionButtonRouteComponent,
    DayComponent,
    DistancePipe,
    LinkRouteComponent,
    SubsetOrphanRouteAnalysisComponent,
  ],
})
export class SubsetOrphanRouteListItemComponent {
  readonly routeType = input.required<RouteType>();
  readonly rowNumber = input.required<number>();
  readonly row = input.required<OrphanRouteInfo>();
}
