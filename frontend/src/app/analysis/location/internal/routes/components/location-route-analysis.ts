import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { LocationRouteInfo } from '@api/common/location';
import { RouteType } from '@api/common';
import { RouteAccessibleIndicatorComponent } from '@app/analysis/components/indicators/route';
import { RouteInvestigateIndicatorComponent } from '@app/analysis/components/indicators/route';

@Component({
  selector: 'kpn-location-route-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <kpn-route-investigate-indicator [investigate]="route().broken" />
      <kpn-route-accessible-indicator
        [accessible]="!route().inaccessible"
        [routeType]="routeType()"
      />
    </div>
  `,
  styles: `
    .analysis {
      display: flex;
    }
  `,
  imports: [RouteAccessibleIndicatorComponent, RouteInvestigateIndicatorComponent],
})
export class LocationRouteAnalysisComponent {
  route = input.required<LocationRouteInfo>();
  routeType = input.required<RouteType>();
}
