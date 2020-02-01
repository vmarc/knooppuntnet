import {Component} from "@angular/core";
import {AnalysisModeService} from "./analysis-mode.service";

@Component({
  selector: "kpn-analysis-canoe-page",
  template: `

    <div>
      <a routerLink="/" class="breadcrumb-link" i18n="@@breadcrumb.home">Home</a>
      <a routerLink="/analysis" class="breadcrumb-link" i18n="@@breadcrumb.analysis">Analysis</a>
      <ng-container i18n="@@network-type.canoe">Canoe</ng-container>
    </div>

    <kpn-page-header i18n="@@network-type.canoe">Canoe</kpn-page-header>

    <kpn-analysis-mode></kpn-analysis-mode>

    <div *ngIf="isModeNetwork() | async">
      <kpn-icon-button routerLink="/analysis/canoe/nl/networks" icon="netherlands" i18n="@@country.nl">Netherlands</kpn-icon-button>
    </div>
    <div *ngIf="isModeLocation() | async">
      <kpn-icon-button routerLink="/analysis/canoe/nl" icon="netherlands" i18n="@@country.nl">Netherlands</kpn-icon-button>
    </div>
  `
})
export class AnalysisCanoePageComponent {

  constructor(private analysisModeService: AnalysisModeService) {
  }

  isModeNetwork() {
    return this.analysisModeService.isModeNetwork;
  }

  isModeLocation() {
    return this.analysisModeService.isModeLocation;
  }

}
