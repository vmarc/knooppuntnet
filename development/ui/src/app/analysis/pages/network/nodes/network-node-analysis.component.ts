import {Component, Input} from "@angular/core";
import {NetworkNodeInfo2} from "../../../../kpn/shared/network/network-node-info2";

@Component({
  selector: 'network-node-analysis',
  template: `
    <kpn-network-indicator></kpn-network-indicator>
    <kpn-route-indicator></kpn-route-indicator>
    <kpn-connection-indicator></kpn-connection-indicator>
    <kpn-role-connection-indicator></kpn-role-connection-indicator>
    <kpn-integrity-indicator></kpn-integrity-indicator>
  `
})
export class NetworkNodeAnalysisComponent {

  @Input() node: NetworkNodeInfo2;

}
