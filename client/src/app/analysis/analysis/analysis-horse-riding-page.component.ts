import {Component} from "@angular/core";

@Component({
  selector: "kpn-analysis-horse-riding-page",
  template: `
    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a> >
      <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a> >
      <ng-container i18n="@@network-type.horse-riding">Horse riding</ng-container>
    </div>

    <kpn-page-header i18n="@@network-type.horse-riding">Horse riding</kpn-page-header>

    <kpn-icon-button
      routerLink="/analysis/horse-riding/nl"
      icon="netherlands"
      text="Netherlands"
      i18n-text="@@country.nl">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/horse-riding/be"
      icon="belgium"
      text="Belgium"
      i18n-text="@@country.be">
    </kpn-icon-button>
  `
})
export class AnalysisHorseRidingPageComponent {
}
