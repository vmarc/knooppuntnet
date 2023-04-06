import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkRouteRow } from '@api/common/network/network-route-row';
import { NetworkType } from '@api/custom/network-type';

@Component({
  selector: 'kpn-network-route-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <kpn-route-investigate-indicator [investigate]="route.investigate" />
      <kpn-route-accessible-indicator
        [accessible]="route.accessible"
        [networkType]="networkType"
      />
      <kpn-route-connection-indicator [route]="route" />
      <kpn-route-proposed-indicator [proposed]="route.proposed" />
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
export class NetworkRouteAnalysisComponent {
  @Input() route: NetworkRouteRow;
  @Input() networkType: NetworkType;
}
