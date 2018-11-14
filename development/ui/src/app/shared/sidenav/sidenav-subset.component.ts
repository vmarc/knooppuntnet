import {Component, Input} from '@angular/core';
import {Subset} from "../../kpn/shared/subset";

@Component({
  selector: 'kpn-sidenav-subset',
  template: `
    <kpn-sidenav-menu title="Analysis details">
      <a mat-list-item routerLink="{{'/analysis/networks/' + subsetParameters()}}">Networks</a>
      <a mat-list-item routerLink="{{'/analysis/facts/' + subsetParameters()}}">Facts</a>
      <a mat-list-item routerLink="{{'/analysis/orphan-nodes/' + subsetParameters()}}">Orphan nodes</a>
      <a mat-list-item routerLink="{{'/analysis/orphan-routes/' + subsetParameters()}}">Orphan routes</a>
      <a mat-list-item routerLink="{{'/analysis/changes/' + subsetParameters()}}">Changes</a>
    </kpn-sidenav-menu>
  `
})
export class SidenavSubsetComponent {

  @Input() subset: Subset;
  @Input() open: boolean;

  subsetParameters() {
    return this.subset.country.domain + "/" + this.subset.networkType.name;
  }

  toggleOpen(): void {
    this.open = !this.open;
  }

}
