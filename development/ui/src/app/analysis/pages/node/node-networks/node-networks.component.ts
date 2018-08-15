import {Component, Input} from "@angular/core";
import {Reference} from "../../../../kpn/shared/common/reference";

@Component({
  selector: 'node-networks',
  templateUrl: './node-networks.component.html',
  styleUrls: ['./node-networks.component.scss']
})
export class NodeNetworksComponent {
  @Input() networks: Array<Reference> = [];
}
