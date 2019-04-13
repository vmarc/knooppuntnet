import {Component, Input} from "@angular/core";
import {Subset} from "../../../kpn/shared/subset";

@Component({
  selector: "kpn-sidebar-subset",
  template: `
    <kpn-sidebar-menu title="Analysis details">
      <kpn-sidebar-sub-item link="{{subsetLink('networks')}}">Networks</kpn-sidebar-sub-item>
      <kpn-sidebar-sub-item link="{{subsetLink('facts')}}">Facts</kpn-sidebar-sub-item>
      <kpn-sidebar-sub-item link="{{subsetLink('orphan-nodes')}}">Orphan nodes</kpn-sidebar-sub-item>
      <kpn-sidebar-sub-item link="{{subsetLink('orphan-routes')}}">Orphan routes</kpn-sidebar-sub-item>
      <kpn-sidebar-sub-item link="{{subsetLink('changes')}}">Changes</kpn-sidebar-sub-item>
    </kpn-sidebar-menu>
  `
})
export class SidebarSubsetComponent {

  @Input() subset: Subset;

  subsetLink(target: string): string {
    return "/analysis/" + target + "/" + this.subset.country.domain + "/" + this.subset.networkType.name;
  }

}
