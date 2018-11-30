import {Component} from '@angular/core';
import {PageService} from "../shared/page.service";

@Component({
  selector: 'kpn-analysis-sidebar',
  template: `
    <kpn-sidebar-subsets *ngIf="showSubsetsMenu" [subset]="subset"></kpn-sidebar-subsets>
    <kpn-sidebar-subset *ngIf="showSubsetMenu" [subset]="subset"></kpn-sidebar-subset>
    <kpn-sidebar-network *ngIf="showNetworkMenu" [networkId]="networkId"></kpn-sidebar-network>
    <kpn-sidebar-footer></kpn-sidebar-footer>
  `
})
export class AnalysisSidebarComponent {

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
