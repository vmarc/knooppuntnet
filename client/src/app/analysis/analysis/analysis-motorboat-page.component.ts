import {Component} from "@angular/core";
import {AnalysisModeService} from "./analysis-mode.service";

@Component({
  selector: "kpn-analysis-motorboat-page",
  template: `

    <div>
      <a routerLink="/" class="breadcrumb-link" i18n="@@breadcrumb.home">Home</a>
      <a routerLink="/analysis" class="breadcrumb-link" i18n="@@breadcrumb.analysis">Analysis</a>
      <ng-container i18n="@@network-type.motorboat">Motorboat</ng-container>
    </div>

    <kpn-page-header i18n="@@network-type.motorboat">Motorboat</kpn-page-header>

    <kpn-analysis-mode></kpn-analysis-mode>

    <div *ngIf="isModeNetwork() | async">
      <kpn-icon-button routerLink="/analysis/motorboat/nl/networks" icon="netherlands" i18n="@@country.nl">Netherlands</kpn-icon-button>
    </div>
    <div *ngIf="isModeLocation() | async">
      <kpn-icon-button routerLink="/analysis/motorboat/nl" icon="netherlands" i18n="@@country.nl">Netherlands</kpn-icon-button>
    </div>
  `
})
export class AnalysisMotorboatPageComponent {

  constructor(public analysisModeService: AnalysisModeService) {
  }

  isModeNetwork() {
    return this.analysisModeService.isModeNetwork;
  }

  isModeLocation() {
    return this.analysisModeService.isModeLocation;
  }

}
