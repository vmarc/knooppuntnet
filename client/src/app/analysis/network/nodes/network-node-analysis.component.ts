import {Component, Input} from "@angular/core";
import {NetworkNodeInfo2} from "../../../kpn/shared/network/network-node-info2";
import {NetworkType} from "../../../kpn/shared/network-type";

@Component({
  selector: "kpn-network-node-analysis",
  template: `
    <kpn-network-indicator [node]="node"></kpn-network-indicator>
    <kpn-route-indicator [node]="node"></kpn-route-indicator>
    <kpn-node-connection-indicator [node]="node"></kpn-node-connection-indicator>
    <kpn-role-connection-indicator [node]="node"></kpn-role-connection-indicator>
    <kpn-integrity-indicator [node]="node" [networkType]="networkType"></kpn-integrity-indicator>
  `
})
export class NetworkNodeAnalysisComponent {

  @Input() node: NetworkNodeInfo2;
  @Input() networkType: NetworkType;

}
