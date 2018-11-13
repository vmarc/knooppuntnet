import {Component, Input} from '@angular/core';
import {Subset} from "../../../kpn/shared/subset";

@Component({
  selector: 'kpn-network-sidenav',
  templateUrl: './network-sidenav.component.html',
  styleUrls: ['./network-sidenav.component.scss']
})
export class NetworkSidenavComponent {

  @Input() subset: Subset;
  @Input() networkId: string;

}
