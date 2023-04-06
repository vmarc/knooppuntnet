import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { LocationRouteInfo } from '@api/common/location/location-route-info';
import { NetworkType } from '@api/custom/network-type';

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
  styles: [
    `
      .analysis {
        display: flex;
      }
    `,
  ],
})
export class LocationRouteAnalysisComponent {
  @Input() route: LocationRouteInfo;
  @Input() networkType: NetworkType;
}
