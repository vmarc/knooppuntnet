import {Component, Input} from "@angular/core";

@Component({
  selector: "kpn-sidebar-network",
  template: `
    <kpn-sidebar-menu title="Network">
      <kpn-sidebar-sub-item link="{{link('details')}}">Details</kpn-sidebar-sub-item>
      <kpn-sidebar-sub-item link="{{link('map')}}">Map</kpn-sidebar-sub-item>
      <kpn-sidebar-sub-item link="{{link('facts')}}">Facts</kpn-sidebar-sub-item>
      <kpn-sidebar-sub-item link="{{link('nodes')}}">Nodes</kpn-sidebar-sub-item>
      <kpn-sidebar-sub-item link="{{link('routes')}}">Routes</kpn-sidebar-sub-item>
      <kpn-sidebar-sub-item link="{{link('changes')}}">Changes</kpn-sidebar-sub-item>
    </kpn-sidebar-menu>
  `
})
export class SidebarNetworkComponent {

  @Input() networkId: string;

  link(target: string): string {
    return "/analysis/network-" + target + "/" + this.networkId;
  }

}
