import {Component, Input} from "@angular/core";
import {NetworkNodeInfo2} from "../../../../kpn/shared/network/network-node-info2";

@Component({
  selector: 'network-node-routes',
  template: `
    node-routes-{{node.id}}
  `
})
export class NetworkNodeRoutesComponent {

  @Input() node: NetworkNodeInfo2;

}
