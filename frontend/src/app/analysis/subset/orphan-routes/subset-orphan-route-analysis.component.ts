import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Input } from '@angular/core';
import { OrphanRouteInfo } from '@api/common';
import { NetworkType } from '@api/custom';
import { RouteAccessibleIndicatorComponent } from '@app/analysis/components/indicators/route';
import { RouteInvestigateIndicatorComponent } from '@app/analysis/components/indicators/route';

@Component({
  selector: 'kpn-subset-orphan-route-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <kpn-route-investigate-indicator [investigate]="route.isBroken" />
      <kpn-route-accessible-indicator [accessible]="route.accessible" [networkType]="networkType" />
    </div>
  `,
  styles: `
    .analysis {
      display: flex;
    }
  `,
  standalone: true,
  imports: [RouteAccessibleIndicatorComponent, RouteInvestigateIndicatorComponent],
})
export class SubsetOrphanRouteAnalysisComponent {
  @Input() route: OrphanRouteInfo;
  @Input() networkType: NetworkType;
}
