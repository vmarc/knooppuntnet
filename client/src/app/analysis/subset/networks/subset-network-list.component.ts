import {Component, Input} from "@angular/core";
import {List} from "immutable";
import {NetworkAttributes} from "../../../kpn/api/common/network/network-attributes";

@Component({
  selector: "kpn-subset-network-list",
  template: `
    <kpn-items>
      <kpn-item *ngFor="let network of networks; let i=index" [index]="i">
        <kpn-subset-network [network]="network"></kpn-subset-network>
      </kpn-item>
    </kpn-items>
  `
})
export class SubsetNetworkListComponent {
  @Input() networks: List<NetworkAttributes>;
}
