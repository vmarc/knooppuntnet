import {Component} from "@angular/core";

@Component({
  selector: "kpn-analysis-be-page",
  template: `

    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a> >
      <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a> >
      <ng-container i18n="@@country.be">Belgium</ng-container>
    </div>

    <h1 i18n="@@country.be">
      Belgium
    </h1>

    <kpn-icon-button
      routerLink="/analysis/networks/be/rcn"
      icon="rcn"
      text="Cycling"
      i18n-text="@@network-type.rcn">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/networks/be/rwn"
      icon="rwn"
      text="Hiking"
      i18n-text="@@network-type.rwn">
    </kpn-icon-button>
  `
})
export class AnalysisBePageComponent {
}
