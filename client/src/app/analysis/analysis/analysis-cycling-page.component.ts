import {Component} from "@angular/core";

@Component({
  selector: "kpn-analysis-cycling-page",
  template: `

    <div>
      <a routerLink="/" class="breadcrumb-link" i18n="@@breadcrumb.home">Home</a>
      <a routerLink="/analysis" class="breadcrumb-link" i18n="@@breadcrumb.analysis">Analysis</a>
      <ng-container i18n="@@network-type.cycling">Cycling</ng-container>
    </div>

    <kpn-page-header i18n="@@network-type.cycling">Cycling</kpn-page-header>

    <kpn-icon-button routerLink="/analysis/cycling/nl" icon="netherlands" i18n="@@country.nl">Netherlands</kpn-icon-button>
    <kpn-icon-button routerLink="/analysis/cycling/be" icon="belgium" i18n="@@country.be">Belgium</kpn-icon-button>
    <kpn-icon-button routerLink="/analysis/cycling/de" icon="germany" i18n="@@country.de">Germany</kpn-icon-button>
    <kpn-icon-button routerLink="/analysis/cycling/fr" icon="france" i18n="@@country.fr">France</kpn-icon-button>
    <kpn-icon-button routerLink="/analysis/cycling/at" icon="austria" i18n="@@country.at">Austria</kpn-icon-button>
  `
})
export class AnalysisCyclingPageComponent {
}
