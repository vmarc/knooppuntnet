import {Component, Input} from "@angular/core";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {NetworkRouteInfo} from "../../../kpn/api/common/network/network-route-info";

@Component({
  selector: "kpn-network-route-analysis",
  template: `
    <kpn-route-investigate-indicator [route]="route"></kpn-route-investigate-indicator>
    <kpn-route-accessible-indicator [route]="route" [networkType]="networkType"></kpn-route-accessible-indicator>
    <kpn-route-connection-indicator [route]="route"></kpn-route-connection-indicator>
  `
})
export class NetworkRouteAnalysisComponent {

  @Input() route: NetworkRouteInfo;
  @Input() networkType: NetworkType;

}
