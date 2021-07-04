import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkNodeDetail } from '@api/common/network/network-node-detail';
import { NetworkScope } from '@api/custom/network-scope';
import { NetworkType } from '@api/custom/network-type';
import { IntegrityIndicatorData } from '../../../components/shared/indicator/integrity-indicator-data';

@Component({
  selector: 'kpn-network-node-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <kpn-network-indicator [node]="node"></kpn-network-indicator>
      <kpn-node-route-indicator [node]="node"></kpn-node-route-indicator>
      <kpn-node-connection-indicator
        [node]="node"
      ></kpn-node-connection-indicator>
      <kpn-role-connection-indicator
        [node]="node"
      ></kpn-role-connection-indicator>
      <kpn-integrity-indicator
        [data]="integrityIndicatorData"
      ></kpn-integrity-indicator>
      <kpn-proposed-indicator
        [node]="node"
      ></kpn-proposed-indicator>
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
export class NetworkNodeAnalysisComponent implements OnInit {
  @Input() networkType: NetworkType;
  @Input() networkScope: NetworkScope;
  @Input() node: NetworkNodeDetail;

  integrityIndicatorData: IntegrityIndicatorData;

  ngOnInit(): void {
    this.integrityIndicatorData = new IntegrityIndicatorData(
      this.networkType,
      this.networkScope,
      this.node.routeReferences.length,
      this.node.expectedRouteCount
    );
  }
}
