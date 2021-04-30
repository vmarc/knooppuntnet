import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { LocationNodeInfo } from '@api/common/location/location-node-info';
import { NetworkScope } from '@api/custom/network-scope';
import { NetworkType } from '@api/custom/network-type';
import { IntegrityIndicatorData } from '../../../components/shared/indicator/integrity-indicator-data';

@Component({
  selector: 'kpn-location-node-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <kpn-location-node-fact-indicator
        [node]="node"
      ></kpn-location-node-fact-indicator>
      <kpn-integrity-indicator
        [data]="integrityIndicatorData"
      ></kpn-integrity-indicator>
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
export class LocationNodeAnalysisComponent implements OnInit {
  @Input() networkType: NetworkType;
  @Input() networkScope: NetworkScope;
  @Input() node: LocationNodeInfo;

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
