import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { NetworkNodeRow } from '@api/common/network';
import { NetworkScope } from '@api/custom';
import { NetworkType } from '@api/custom';
import { IntegrityIndicatorData } from '@app/components/shared/indicator';

@Component({
  selector: 'kpn-network-node-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <kpn-network-indicator [node]="node" />
      <kpn-node-connection-indicator [node]="node" />
      <kpn-role-connection-indicator [node]="node" />
      <kpn-integrity-indicator [data]="integrityIndicatorData" />
      <kpn-proposed-indicator [node]="node" />
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
  @Input() node: NetworkNodeRow;

  integrityIndicatorData: IntegrityIndicatorData;

  ngOnInit(): void {
    let expectedRouteCount = '-';
    if (this.node.detail.expectedRouteCount) {
      expectedRouteCount = this.node.detail.expectedRouteCount.toString();
    }
    this.integrityIndicatorData = new IntegrityIndicatorData(
      this.networkType,
      this.networkScope,
      this.node.routeReferences.length,
      expectedRouteCount
    );
  }
}
