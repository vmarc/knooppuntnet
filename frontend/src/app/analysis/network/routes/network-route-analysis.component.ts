import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { NetworkRouteRow } from '@api/common/network';
import { NetworkType } from '@api/custom';
import { RouteAccessibleIndicatorComponent } from '@app/analysis/components/indicators/route';
import { RouteConnectionIndicatorComponent } from '@app/analysis/components/indicators/route';
import { RouteInvestigateIndicatorComponent } from '@app/analysis/components/indicators/route';
import { RouteProposedIndicatorComponent } from '@app/analysis/components/indicators/route';

@Component({
  selector: 'kpn-network-route-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <kpn-route-investigate-indicator [investigate]="route().investigate" />
      <kpn-route-accessible-indicator
        [accessible]="route().accessible"
        [networkType]="networkType()"
      />
      <kpn-route-connection-indicator [route]="route()" />
      <kpn-route-proposed-indicator [proposed]="route().proposed" />
    </div>
  `,
  styles: `
    .analysis {
      display: flex;
    }
  `,
  standalone: true,
  imports: [
    RouteAccessibleIndicatorComponent,
    RouteConnectionIndicatorComponent,
    RouteInvestigateIndicatorComponent,
    RouteProposedIndicatorComponent,
  ],
})
export class NetworkRouteAnalysisComponent {
  route = input.required<NetworkRouteRow>();
  networkType = input.required<NetworkType>();
}
