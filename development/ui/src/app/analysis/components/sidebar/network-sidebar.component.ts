import {Component, Input} from '@angular/core';
import {Subset} from "../../../kpn/shared/subset";

@Component({
  selector: 'kpn-network-sidebar',
  template: `
    <kpn-sidebar-subsets [subset]="subset"></kpn-sidebar-subsets>
    <kpn-sidebar-network [networkId]="networkId"></kpn-sidebar-network>
    <kpn-sidebar-footer></kpn-sidebar-footer>
  `
})
export class NetworkSidebarComponent {

  @Input() subset: Subset;
  @Input() networkId: string;

}
