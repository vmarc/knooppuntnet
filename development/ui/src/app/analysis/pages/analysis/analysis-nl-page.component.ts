import {Component} from "@angular/core";

@Component({
  selector: "kpn-analysis-nl-page",
  template: `

    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a> >
      <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a> >
      <ng-container i18n="@@country.nl">The Netherlands</ng-container>
    </div>

    <h1 i18n="@@country.nl">
      The Netherlands
    </h1>

    <kpn-icon-button
      routerLink="/analysis/networks/nl/rcn"
      icon="rcn"
      text="Cycling"
      i18n-text="@@network-type.rcn">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/networks/nl/rwn"
      icon="rwn"
      text="Hiking"
      i18n-text="@@network-type.rwn">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/networks/nl/rhn"
      icon="rhn"
      text="Horse"
      i18n-text="@@network-type.rhn">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/networks/nl/rmn"
      icon="rmn"
      text="Motorboat"
      i18n-text="@@network-type.rmn">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/networks/nl/rpn"
      icon="rpn"
      text="Canoe"
      i18n-text="@@network-type.rpn">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/networks/nl/rin"
      icon="rin"
      text="Inline skating"
      i18n-text="@@network-type.rin">
    </kpn-icon-button>
  `
})
export class AnalysisNlPageComponent {
}
