import {Component, OnInit} from '@angular/core';
import {PageService} from "../../../components/shared/page.service";

@Component({
  selector: 'kpn-home-page',
  template: `
    <h1 i18n="@@home-page-title">
      Node networks
    </h1>

    <kpn-icon-button
      routerLink="/map"
      icon="map"
      text="Map">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis"
      icon="analysis"
      text="Analysis">
    </kpn-icon-button>

    <div class="test-section">
      <h2>Test route directions</h2>
      <div>
        <a routerLink="/map/directions/example-1">example 1</a>
      </div>
    </div>
  `,
  styles: [`
    .test-section {
      margin-top: 100px;
    }
  `]
})
export class HomePageComponent implements OnInit {

  constructor(private pageService: PageService) {
  }

  ngOnInit() {
    this.pageService.defaultMenu();
  }

}
