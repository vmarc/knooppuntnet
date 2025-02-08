import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { OrphanRouteInfo } from '@api/common/orphan-route-info';
import { RouteType } from '@api/common/route-type';
import { RouteAccessibleIndicatorComponent } from '@app/analysis/components/indicators/route';
import { RouteInvestigateIndicatorComponent } from '@app/analysis/components/indicators/route';

@Component({
  selector: 'kpn-subset-orphan-route-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <kpn-route-investigate-indicator [investigate]="route().isBroken" />
      <kpn-route-accessible-indicator [accessible]="route().accessible" [routeType]="routeType()" />
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
  route = input.required<OrphanRouteInfo>();
  routeType = input.required<RouteType>();
}
