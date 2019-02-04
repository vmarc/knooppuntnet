import {Component, OnInit} from '@angular/core';
import {PageService} from "../../../shared/page.service";

@Component({
  selector: 'kpn-home-page',
  template: `
    <h1 i18n="@@home-page-title">
      Node networks
    </h1>

    <div class="kpn-line">
      <mat-icon svgIcon="planner"></mat-icon>
      <a routerLink="/planner">
        <span>Planner</span>
      </a>
    </div>

    <div class="kpn-line">
      <mat-icon svgIcon="analysis"></mat-icon>
      <a routerLink="/analysis">
        <span>Analysis</span>
      </a>
    </div>
  `
})
export class HomePageComponent implements OnInit {

  constructor(private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
  }

}
