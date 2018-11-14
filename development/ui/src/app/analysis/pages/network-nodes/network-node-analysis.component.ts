import {Component, Input} from "@angular/core";
import {NetworkNodeInfo2} from "../../../kpn/shared/network/network-node-info2";

@Component({
  selector: 'network-node-analysis',
  template: `
    node-analysis-{{node.id}}
  `
})
export class NetworkNodeAnalysisComponent {

  @Input() node: NetworkNodeInfo2;

}
