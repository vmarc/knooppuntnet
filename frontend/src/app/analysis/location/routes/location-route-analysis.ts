import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { LocationRouteInfo } from '@api/common/location';
import { NetworkType } from '@api/custom';
import { RouteAccessibleIndicatorComponent } from '@app/analysis/components/indicators/route';
import { RouteInvestigateIndicatorComponent } from '@app/analysis/components/indicators/route';

@Component({
  selector: 'kpn-location-route-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <kpn-route-investigate-indicator [investigate]="route.broken" />
      <kpn-route-accessible-indicator
        [accessible]="!route.inaccessible"
        [networkType]="networkType"
      />
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
    RouteInvestigateIndicatorComponent,
  ],
})
export class LocationRouteAnalysisComponent {
  @Input() route: LocationRouteInfo;
  @Input() networkType: NetworkType;
}
