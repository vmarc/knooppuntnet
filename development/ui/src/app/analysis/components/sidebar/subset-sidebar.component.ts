import {Component, Input} from '@angular/core';
import {Subset} from "../../../kpn/shared/subset";

@Component({
  selector: 'kpn-subset-sidebar',
  template: `
    <kpn-sidebar-subsets [subset]="subset"></kpn-sidebar-subsets>
    <kpn-sidebar-subset [subset]="subset"></kpn-sidebar-subset>
    <kpn-sidebar-footer></kpn-sidebar-footer>
  `
})
export class SubsetSidebarComponent {

  @Input() subset: Subset;

}
