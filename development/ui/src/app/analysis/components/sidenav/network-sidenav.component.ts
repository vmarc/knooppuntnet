import {Component, Input} from '@angular/core';
import {Subset} from "../../../kpn/shared/subset";

@Component({
  selector: 'kpn-network-sidenav',
  template: `
    <kpn-sidenav-subsets [subset]="subset"></kpn-sidenav-subsets>
    <kpn-sidenav-network [networkId]="networkId"></kpn-sidenav-network>
    <kpn-sidenav-footer></kpn-sidenav-footer>
  `
})
export class NetworkSidenavComponent {

  @Input() subset: Subset;
  @Input() networkId: string;

}
