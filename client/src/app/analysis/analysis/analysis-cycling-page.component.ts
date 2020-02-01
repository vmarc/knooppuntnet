import {Component} from "@angular/core";
import {AnalysisModeService} from "./analysis-mode.service";
import {Observable} from "rxjs";

@Component({
  selector: "kpn-analysis-cycling-page",
  template: `

    <div>
      <a routerLink="/" class="breadcrumb-link" i18n="@@breadcrumb.home">Home</a>
      <a routerLink="/analysis" class="breadcrumb-link" i18n="@@breadcrumb.analysis">Analysis</a>
      <ng-container i18n="@@network-type.cycling">Cycling</ng-container>
    </div>

    <kpn-page-header i18n="@@network-type.cycling">Cycling</kpn-page-header>

    <kpn-analysis-mode></kpn-analysis-mode>

    <div *ngIf="isModeNetwork() | async">
      <kpn-icon-button routerLink="/analysis/cycling/nl/networks" icon="netherlands" i18n="@@country.nl">Netherlands</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/cycling/be/networks" icon="belgium" i18n="@@country.be">Belgium</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/cycling/de/networks" icon="germany" i18n="@@country.de">Germany</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/cycling/fr/networks" icon="france" i18n="@@country.fr">France</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/cycling/at/networks" icon="austria" i18n="@@country.at">Austria</kpn-icon-button>
    </div>
    <div *ngIf="isModeLocation() | async">
      <kpn-icon-button routerLink="/analysis/cycling/nl" icon="netherlands" i18n="@@country.nl">Netherlands</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/cycling/be" icon="belgium" i18n="@@country.be">Belgium</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/cycling/de" icon="germany" i18n="@@country.de">Germany</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/cycling/fr" icon="france" i18n="@@country.fr">France</kpn-icon-button>
      <kpn-icon-button routerLink="/analysis/cycling/at" icon="austria" i18n="@@country.at">Austria</kpn-icon-button>
    </div>
  `
})
export class AnalysisCyclingPageComponent {

  constructor(public analysisModeService: AnalysisModeService) {
  }

  isModeNetwork() {
    return this.analysisModeService.isModeNetwork;
  }

  isModeLocation() {
    return this.analysisModeService.isModeLocation;
  }

}
