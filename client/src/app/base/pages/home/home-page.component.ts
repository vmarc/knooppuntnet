import {Component, OnInit} from "@angular/core";
import {PageService} from "../../../components/shared/page.service";

@Component({
  selector: "kpn-home-page",
  template: `

    <kpn-page-header subject="" i18n="@@home.page-title">Node networks</kpn-page-header>

    <kpn-icon-button
      routerLink="/map"
      icon="map"
      text="Map"
      i18n-text="@@home.map">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis"
      icon="analysis"
      text="Analysis"
      i18n-text="@@home.analysis">
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
