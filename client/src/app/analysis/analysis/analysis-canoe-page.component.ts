import {Component} from "@angular/core";

@Component({
  selector: "kpn-analysis-canoe-page",
  template: `

    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a> >
      <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a> >
      <ng-container i18n="@@network-type.canoe">Canoe</ng-container>
    </div>

    <kpn-page-header i18n="@@network-type.canoe">Canoe</kpn-page-header>

    <kpn-icon-button
      routerLink="/analysis/canoe/nl"
      icon="netherlands"
      text="Netherlands"
      i18n-text="@@country.nl">
    </kpn-icon-button>
  `
})
export class AnalysisCanoePageComponent {
}
