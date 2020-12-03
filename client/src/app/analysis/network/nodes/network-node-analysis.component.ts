import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {NetworkInfoNode} from '../../../kpn/api/common/network/network-info-node';
import {NetworkType} from '../../../kpn/api/custom/network-type';

@Component({
  selector: 'kpn-network-node-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <kpn-network-indicator [node]="node"></kpn-network-indicator>
      <kpn-node-route-indicator [node]="node"></kpn-node-route-indicator>
      <kpn-node-connection-indicator [node]="node"></kpn-node-connection-indicator>
      <kpn-role-connection-indicator [node]="node"></kpn-role-connection-indicator>
      <kpn-integrity-indicator [node]="node" [networkType]="networkType"></kpn-integrity-indicator>
    </div>
  `,
  styles: [`
    .analysis {
      display: flex;
    }
  `]
})
export class NetworkNodeAnalysisComponent {

  @Input() node: NetworkInfoNode;
  @Input() networkType: NetworkType;

}
