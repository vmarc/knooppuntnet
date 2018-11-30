import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-sidenav-network',
  template: `
    <kpn-sidenav-menu title="Network">
      <kpn-sidenav-sub-item link="{{link('details')}}">Details</kpn-sidenav-sub-item>
      <kpn-sidenav-sub-item link="{{link('map')}}">Map</kpn-sidenav-sub-item>
      <kpn-sidenav-sub-item link="{{link('facts')}}">Facts</kpn-sidenav-sub-item>
      <kpn-sidenav-sub-item link="{{link('nodes')}}">Nodes</kpn-sidenav-sub-item>
      <kpn-sidenav-sub-item link="{{link('routes')}}">Routes</kpn-sidenav-sub-item>
      <kpn-sidenav-sub-item link="{{link('changes')}}">Changes</kpn-sidenav-sub-item>
    </kpn-sidenav-menu>
  `
})
export class SidenavNetworkComponent {

  @Input() networkId: string;

  link(target: string): string {
    return "/analysis/network-" + target + "/" + this.networkId;
  }

}
