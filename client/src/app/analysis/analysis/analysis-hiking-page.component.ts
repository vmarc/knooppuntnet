import {Component} from "@angular/core";
import {AnalysisModeService} from "./analysis-mode.service";

@Component({
  selector: "kpn-analysis-hiking-page",
  template: `
    <div>
      <a routerLink="/" class="breadcrumb-link" i18n="@@breadcrumb.home">Home</a>
      <a routerLink="/analysis" class="breadcrumb-link" i18n="@@breadcrumb.analysis">Analysis</a>
      <ng-container i18n="@@network-type.hiking">Hiking</ng-container>
    </div>

    <kpn-page-header i18n="@@network-type.hiking">Hiking</kpn-page-header>

    <kpn-analysis-mode></kpn-analysis-mode>

    <div *ngIf="isModeNetwork() | async">
      <kpn-icon-button routerLink="/analysis/hiking/nl/networks" icon="netherlands" i18n="@@country.nl">Netherlands</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/hiking/be/networks" icon="belgium" i18n="@@country.be">Belgium</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/hiking/de/networks" icon="germany" i18n="@@country.de">Germany</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/hiking/fr/networks" icon="france" i18n="@@country.fr">France</kpn-icon-button>
    </div>
    <div *ngIf="isModeLocation() | async">
      <kpn-icon-button routerLink="/analysis/hiking/nl" icon="netherlands" i18n="@@country.nl">Netherlands</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/hiking/be" icon="belgium" i18n="@@country.be">Belgium</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/hiking/de" icon="germany" i18n="@@country.de">Germany</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/hiking/fr" icon="france" i18n="@@country.fr">France</kpn-icon-button>
    </div>
  `
})
export class AnalysisHikingPageComponent {

  constructor(public analysisModeService: AnalysisModeService) {
  }

  isModeNetwork() {
    return this.analysisModeService.isModeNetwork;
  }

  isModeLocation() {
    return this.analysisModeService.isModeLocation;
  }

}
