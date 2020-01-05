import {Component} from "@angular/core";

@Component({
  selector: "kpn-analysis-cycling-page",
  template: `

    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a> >
      <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a> >
      <ng-container i18n="@@network-type.cycling">Cycling</ng-container>
    </div>

    <kpn-page-header i18n="@@network-type.cycling">Cycling</kpn-page-header>

    <kpn-icon-button
      routerLink="/analysis/cycling/nl"
      icon="netherlands"
      text="Netherlands"
      i18n-text="@@country.nl">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/cycling/be"
      icon="belgium"
      text="Belgium"
      i18n-text="@@country.be">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/cycling/de"
      icon="germany"
      text="Germany"
      i18n-text="@@country.de">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/cycling/fr"
      icon="france"
      text="France"
      i18n-text="@@country.fr">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/cycling/at"
      icon="austria"
      text="Austria"
      i18n-text="@@country.at">
    </kpn-icon-button>
  `
})
export class AnalysisCyclingPageComponent {
}
