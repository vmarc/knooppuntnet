import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-sidenav-network',
  templateUrl: './sidenav-network.component.html',
  styleUrls: ['./sidenav-network.component.scss']
})
export class SidenavNetworkComponent {

  @Input() networkId: string;

}
