import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {NodeNetworkReference} from "../../../kpn/api/common/node/node-network-reference";
import {NodeInfo} from "../../../kpn/api/common/node-info";

@Component({
  selector: "kpn-node-network-references",
  template: `
    <p *ngIf="references.isEmpty()" i18n="@@node.network-references.none">None</p>
    <p *ngFor="let reference of references">
      <kpn-node-network-reference [nodeInfo]="nodeInfo" [reference]="reference"></kpn-node-network-reference>
    </p>
  `
})
export class NodeNetworkReferencesComponent {
  @Input() nodeInfo: NodeInfo;
  @Input() references: List<NodeNetworkReference>;
}
