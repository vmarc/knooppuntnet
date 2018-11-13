import {Component, Input} from '@angular/core';
import {Subset} from "../../kpn/shared/subset";

@Component({
  selector: 'kpn-sidenav-subset',
  templateUrl: './sidenav-subset.component.html',
  styleUrls: ['./sidenav-subset.component.scss']
})
export class SidenavSubsetComponent {

  @Input() subset: Subset;
  @Input() open: boolean;

  subsetParameters() {
    return this.subset.country.domain + "/" + this.subset.networkType.name;
  }

  toggleOpen(): void {
    this.open = ! this.open;
  }

}
