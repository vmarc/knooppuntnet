import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { RouteScope } from '@api/common/route-scope';
import { RouteType } from '@api/common/route-type';
import { NetworkNodeRow } from '@api/common/network/network-node-row';
import { IntegrityIndicatorData } from '@app/shared/components/indicator/integrity-indicator-data';
import { IntegrityIndicatorComponent } from '@app/shared/components/indicator/integrity-indicator.component';
import { NetworkIndicatorComponent } from './indicators/network-indicator.component';
import { NodeConnectionIndicatorComponent } from './indicators/node-connection-indicator.component';
import { ProposedIndicatorComponent } from './indicators/proposed-indicator.component';
import { RoleConnectionIndicatorComponent } from './indicators/role-connection-indicator.component';

@Component({
  selector: 'ui-network-node-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <ui-network-indicator [node]="node()" />
      <ui-node-connection-indicator [node]="node()" />
      <ui-role-connection-indicator [node]="node()" />
      <ui-integrity-indicator [data]="integrityIndicatorData" />
      <ui-proposed-indicator [node]="node()" />
    </div>
  `,
  styles: `
    .analysis {
      display: flex;
    }
  `,
  imports: [
    IntegrityIndicatorComponent,
    NetworkIndicatorComponent,
    NodeConnectionIndicatorComponent,
    ProposedIndicatorComponent,
    RoleConnectionIndicatorComponent,
  ],
})
export class NetworkNodeAnalysisComponent implements OnInit {
  readonly routeType = input.required<RouteType>();
  readonly routeScope = input.required<RouteScope>();
  readonly node = input.required<NetworkNodeRow>();

  integrityIndicatorData: IntegrityIndicatorData;

  ngOnInit(): void {
    let expectedRouteCount = '-';
    if (this.node().detail.expectedRouteCount) {
      expectedRouteCount = this.node().detail.expectedRouteCount.toString();
    }
    this.integrityIndicatorData = new IntegrityIndicatorData(
      this.routeType(),
      this.routeScope(),
      this.node().routeReferences.length,
      expectedRouteCount
    );
  }
}
