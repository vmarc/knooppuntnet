import {Component, Input} from '@angular/core';
import {Subset} from "../../kpn/shared/subset";

@Component({
  selector: 'kpn-sidenav-subset',
  template: `
    <kpn-sidenav-menu title="Analysis details">
      <kpn-sidenav-sub-item link="{{subsetLink('networks')}}">Networks</kpn-sidenav-sub-item>
      <kpn-sidenav-sub-item link="{{subsetLink('facts')}}">Facts</kpn-sidenav-sub-item>
      <kpn-sidenav-sub-item link="{{subsetLink('orphan-nodes')}}">Orphan nodes</kpn-sidenav-sub-item>
      <kpn-sidenav-sub-item link="{{subsetLink('orphan-routes')}}">Orphan routes</kpn-sidenav-sub-item>
      <kpn-sidenav-sub-item link="{{subsetLink('changes')}}">Changes</kpn-sidenav-sub-item>
    </kpn-sidenav-menu>
  `
})
export class SidenavSubsetComponent {

  @Input() subset: Subset;

  subsetLink(target: string): string {
    return "/analysis/" + target + "/" + this.subset.country.domain + "/" + this.subset.networkType.name;
  }

}
