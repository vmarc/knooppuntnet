import {Component, Input} from "@angular/core";
import {NetworkNodeInfo2} from "../../../../kpn/shared/network/network-node-info2";

@Component({
  selector: 'network-node-analysis',
  templateUrl: './network-node-analysis.component.html',
  styleUrls: ['./network-node-analysis.component.scss']
})
export class NetworkNodeAnalysisComponent {

  @Input() node: NetworkNodeInfo2;

}
