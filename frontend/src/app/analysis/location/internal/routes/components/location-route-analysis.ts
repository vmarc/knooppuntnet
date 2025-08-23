import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { LocationRouteInfo } from '@api/common/location/location-route-info';
import { RouteType } from '@api/common/route-type';
import { RouteAccessibleIndicatorComponent } from '@app/analysis/components/indicators/route/route-accessible-indicator.component';
import { RouteInvestigateIndicatorComponent } from '@app/analysis/components/indicators/route/route-investigate-indicator.component';

@Component({
  selector: 'ui-location-route-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <ui-route-investigate-indicator [investigate]="route().broken" />
      <ui-route-accessible-indicator
        [inaccessible]="!route().inaccessible"
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
  readonly route = input.required<LocationRouteInfo>();
  readonly routeType = input.required<RouteType>();
}
