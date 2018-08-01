import {Component, Input} from "@angular/core";
import {NetworkNodeInfo2} from "../../../../kpn/shared/network/network-node-info2";

@Component({
  selector: 'network-node-routes',
  templateUrl: './network-node-routes.component.html',
  styleUrls: ['./network-node-routes.component.scss']
})
export class NetworkNodeRoutesComponent {

  @Input() node: NetworkNodeInfo2;

}
