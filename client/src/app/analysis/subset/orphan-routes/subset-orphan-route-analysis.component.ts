import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { OrphanRouteInfo } from '@api/common';
import { NetworkType } from '@api/custom';

@Component({
  selector: 'kpn-subset-orphan-route-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <kpn-route-investigate-indicator [investigate]="route.isBroken" />
      <kpn-route-accessible-indicator
        [accessible]="route.accessible"
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
export class SubsetOrphanRouteAnalysisComponent {
  @Input() route: OrphanRouteInfo;
  @Input() networkType: NetworkType;
}
