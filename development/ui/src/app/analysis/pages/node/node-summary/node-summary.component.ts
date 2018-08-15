import {Component, Input} from "@angular/core";
import {NodeInfo} from "../../../../kpn/shared/node-info";

@Component({
  selector: 'node-summary',
  templateUrl: './node-summary.component.html',
  styleUrls: ['./node-summary.component.scss']
})
export class NodeSummaryComponent {
  @Input() nodeInfo: NodeInfo;
}
