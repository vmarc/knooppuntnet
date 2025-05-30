import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkRouteRow } from '@api/common/network/network-route-row';
import { RouteType } from '@api/common/route-type';
import { RouteAccessibleIndicatorComponent } from '@app/analysis/components/indicators/route/route-accessible-indicator.component';
import { RouteConnectionIndicatorComponent } from '@app/analysis/components/indicators/route/route-connection-indicator.component';
import { RouteInvestigateIndicatorComponent } from '@app/analysis/components/indicators/route/route-investigate-indicator.component';
import { RouteProposedIndicatorComponent } from '@app/analysis/components/indicators/route/route-proposed-indicator.component';

@Component({
  selector: 'ui-network-route-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <ui-route-investigate-indicator [investigate]="route().investigate" />
      <ui-route-accessible-indicator [accessible]="route().accessible" [routeType]="routeType()" />
      <ui-route-connection-indicator [route]="route()" />
      <ui-route-proposed-indicator [proposed]="route().proposed" />
    </div>
  `,
  styles: `
    .analysis {
      display: flex;
    }
  `,
  imports: [
    RouteAccessibleIndicatorComponent,
    RouteConnectionIndicatorComponent,
    RouteInvestigateIndicatorComponent,
    RouteProposedIndicatorComponent,
  ],
})
export class NetworkRouteAnalysisComponent {
  route = input.required<NetworkRouteRow>();
  routeType = input.required<RouteType>();
}
