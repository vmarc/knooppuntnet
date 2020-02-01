import {Component} from "@angular/core";
import {AnalysisModeService} from "./analysis-mode.service";

@Component({
  selector: "kpn-analysis-horse-riding-page",
  template: `
    <div>
      <a routerLink="/" class="breadcrumb-link" i18n="@@breadcrumb.home">Home</a>
      <a routerLink="/analysis" class="breadcrumb-link" i18n="@@breadcrumb.analysis">Analysis</a>
      <ng-container i18n="@@network-type.horse-riding">Horse riding</ng-container>
    </div>

    <kpn-page-header i18n="@@network-type.horse-riding">Horse riding</kpn-page-header>

    <kpn-analysis-mode></kpn-analysis-mode>

    <div *ngIf="isModeNetwork() | async">
      <kpn-icon-button routerLink="/analysis/horse-riding/nl/networks" icon="netherlands" i18n="@@country.nl">Netherlands</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/horse-riding/be/networks" icon="belgium" i18n="@@country.be">Belgium</kpn-icon-button>
    </div>
    <div *ngIf="isModeLocation() | async">
      <kpn-icon-button routerLink="/analysis/horse-riding/nl" icon="netherlands" i18n="@@country.nl">Netherlands</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/horse-riding/be" icon="belgium" i18n="@@country.be">Belgium</kpn-icon-button>
    </div>
  `
})
export class AnalysisHorseRidingPageComponent {

  constructor(public analysisModeService: AnalysisModeService) {
  }

  isModeNetwork() {
    return this.analysisModeService.isModeNetwork;
  }

  isModeLocation() {
    return this.analysisModeService.isModeLocation;
  }

}
