import {Component, Input} from '@angular/core';
import {Subset} from "../../../kpn/shared/subset";

@Component({
  selector: 'kpn-subset-sidenav',
  template: `
    <kpn-sidenav-subsets [subset]="subset"></kpn-sidenav-subsets>
    <kpn-sidenav-subset [subset]="subset"></kpn-sidenav-subset>
    <kpn-sidenav-footer></kpn-sidenav-footer>
  `
})
export class SubsetSidenavComponent {

  @Input() subset: Subset;

}
