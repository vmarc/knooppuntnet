import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { OrphanRouteInfo } from '@api/common/orphan-route-info';
import { RouteType } from '@api/common/route-type';
import { RouteAccessibleIndicatorComponent } from '@app/analysis/components/indicators/route/route-accessible-indicator.component';
import { RouteInvestigateIndicatorComponent } from '@app/analysis/components/indicators/route/route-investigate-indicator.component';

@Component({
  selector: 'ui-subset-orphan-route-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <ui-route-investigate-indicator [investigate]="route().isBroken" />
      <ui-route-accessible-indicator [accessible]="route().accessible" [routeType]="routeType()" />
    </div>
  `,
  styles: `
    .analysis {
      display: flex;
    }
  `,
  imports: [RouteAccessibleIndicatorComponent, RouteInvestigateIndicatorComponent],
})
export class SubsetOrphanRouteAnalysisComponent {
  readonly route = input.required<OrphanRouteInfo>();
  readonly routeType = input.required<RouteType>();
}
