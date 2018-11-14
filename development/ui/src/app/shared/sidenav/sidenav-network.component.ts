import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-sidenav-network',
  template: `
    <kpn-sidenav-menu title="Network">
      <a mat-list-item routerLink="{{'/analysis/network-details/' + networkId}}">Details</a>
      <a mat-list-item routerLink="{{'/analysis/network-map/' + networkId}}">Map</a>
      <a mat-list-item routerLink="{{'/analysis/network-facts/' + networkId}}">Facts</a>
      <a mat-list-item routerLink="{{'/analysis/network-nodes/' + networkId}}">Nodes</a>
      <a mat-list-item routerLink="{{'/analysis/network-routes/' + networkId}}">Routes</a>
      <a mat-list-item routerLink="{{'/analysis/network-changes/' + networkId}}">Changes</a>
    </kpn-sidenav-menu>
  `
})
export class SidenavNetworkComponent {

  @Input() networkId: string;

}
