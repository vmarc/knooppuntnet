import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {NetworkRouteRow} from '../../../kpn/api/common/network/network-route-row';
import {NetworkType} from '../../../kpn/api/custom/network-type';

@Component({
  selector: 'kpn-network-route-analysis',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="analysis">
      <kpn-route-investigate-indicator [route]="route"></kpn-route-investigate-indicator>
      <kpn-route-accessible-indicator [route]="route" [networkType]="networkType"></kpn-route-accessible-indicator>
      <kpn-route-connection-indicator [route]="route"></kpn-route-connection-indicator>
    </div>
  `,
  styles: [`
    .analysis {
      display: flex;
    }
  `]
})
export class NetworkRouteAnalysisComponent {

  @Input() route: NetworkRouteRow;
  @Input() networkType: NetworkType;

}
