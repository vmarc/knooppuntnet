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

    <div class="test-section">
      <h2>Test map features</h2>
      <div>
        <a routerLink="/map/tryout1">Openlayers measure</a>
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
