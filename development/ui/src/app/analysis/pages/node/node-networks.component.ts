import {Component, Input} from "@angular/core";
import {Reference} from "../../../kpn/shared/common/reference";

@Component({
  selector: 'node-networks',
  template: `
    <p *ngIf="networks.length == 0">None</p> <!-- Geen -->
    <p *ngFor="let network of networks">
      <icon-network-link [network]="network"></icon-network-link>
    </p>
  `
})
export class NodeNetworksComponent {
  @Input() networks: Array<Reference> = [];
}
