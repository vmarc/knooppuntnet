import {Component, OnInit} from '@angular/core';
import {PageService} from "../../../shared/page.service";

@Component({
  selector: 'kpn-home-page',
  template: `
    <h1 i18n="@@home-page-title">
      Node networks
    </h1>

    <kpn-icon-button
      routerLink="/planner"
      icon="planner"
      text="Planner">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis"
      icon="analysis"
      text="Analysis">
    </kpn-icon-button>

  `
})
export class HomePageComponent implements OnInit {

  constructor(private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
  }

}
