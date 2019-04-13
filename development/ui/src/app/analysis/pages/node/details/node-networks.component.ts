import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {NodeNetworkReference} from "../../../../kpn/shared/node/node-network-reference";

@Component({
  selector: "node-networks",
  template: `
    <p *ngIf="networkReferences.isEmpty()">None</p> <!-- Geen -->
    <p *ngFor="let networkReference of networkReferences">
      <icon-network-link [network]="networkReference.networkType"></icon-network-link>
    </p>
  `
})
export class NodeNetworksComponent {
  @Input() networkReferences: List<NodeNetworkReference>;
}
