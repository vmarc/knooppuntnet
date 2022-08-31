import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { OrphanRouteInfo } from '@api/common/orphan-route-info';
import { NetworkType } from '@api/custom/network-type';

@Component({
  selector: 'kpn-subset-orphan-route-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <kpn-route-investigate-indicator
        [investigate]="route.isBroken"
      ></kpn-route-investigate-indicator>
      <kpn-route-accessible-indicator
        [accessible]="route.accessible"
        [networkType]="networkType"
      ></kpn-route-accessible-indicator>
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
export class SubsetOrphanRouteAnalysisComponent {
  @Input() route: OrphanRouteInfo;
  @Input() networkType: NetworkType;
}
