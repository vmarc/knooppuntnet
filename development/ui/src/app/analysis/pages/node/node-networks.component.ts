import {Component, Input} from "@angular/core";
import {Reference} from "../../../kpn/shared/common/reference";
import {List} from "immutable";

@Component({
  selector: 'node-networks',
  template: `
    <p *ngIf="networks.isEmpty()">None</p> <!-- Geen -->
    <p *ngFor="let network of networks">
      <icon-network-link [network]="network"></icon-network-link>
    </p>
  `
})
export class NodeNetworksComponent {
  @Input() networks: List<Reference> = List();
}
