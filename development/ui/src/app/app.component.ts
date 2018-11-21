import {Component} from '@angular/core';
import {PageService} from "./shared/page.service";

@Component({
  selector: 'app-root',
  template: `
    <kpn-page>
      <kpn-toolbar toolbar></kpn-toolbar>
      <div sidenav>
        <kpn-sidenav-subsets *ngIf="showSubsetsMenu" [subset]="subset"></kpn-sidenav-subsets>
        <kpn-sidenav-subset *ngIf="showSubsetMenu" [subset]="subset"></kpn-sidenav-subset>
        <kpn-sidenav-network *ngIf="showNetworkMenu" [networkId]="networkId"></kpn-sidenav-network>
        <kpn-sidenav></kpn-sidenav>
        <kpn-sidenav-footer></kpn-sidenav-footer>
      </div>
      <div content>
        <router-outlet></router-outlet>
      </div>
    </kpn-page>
  `
})
export class AppComponent {

  constructor(private pageService: PageService) {
  }

  get subset() {
    return this.pageService.subset;
  }

  get networkId() {
    return this.pageService.networkId;
  }

  get showSubsetsMenu() {
    return this.pageService.showSubsetsMenu;
  }

  get showSubsetMenu() {
    return this.pageService.showSubsetMenu;
  }

  get showNetworkMenu() {
    return this.pageService.showNetworkMenu;
  }

}
