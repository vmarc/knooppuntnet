import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {LocationRouteInfo} from '@api/common/location/location-route-info';
import {NetworkType} from '@api/custom/network-type';

@Component({
  selector: 'kpn-location-route-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <kpn-route-investigate-indicator [investigate]="route.broken"></kpn-route-investigate-indicator>
      <kpn-route-accessible-indicator [accessible]="route.accessible" [networkType]="networkType"></kpn-route-accessible-indicator>
    </div>
  `,
  styles: [`
    .analysis {
      display: flex;
    }
  `]
})
export class LocationRouteAnalysisComponent {

  @Input() route: LocationRouteInfo;
  @Input() networkType: NetworkType;

}
