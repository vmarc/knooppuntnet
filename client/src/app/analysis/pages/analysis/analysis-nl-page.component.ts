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
      routerLink="/analysis/nl/cycling/networks"
      icon="cycling"
      text="Cycling"
      i18n-text="@@network-type.cycling">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/nl/hiking/networks"
      icon="hiking"
      text="Hiking"
      i18n-text="@@network-type.hiking">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/nl/horse/networks"
      icon="horse"
      text="Horse"
      i18n-text="@@network-type.horse">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/nl/motorboat/networks"
      icon="motorboat"
      text="Motorboat"
      i18n-text="@@network-type.motorboat">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/nl/canoe/networks"
      icon="canoe"
      text="Canoe"
      i18n-text="@@network-type.canoe">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/nl/inline-skating/networks"
      icon="inline-skating"
      text="Inline skating"
      i18n-text="@@network-type.inline-skating">
    </kpn-icon-button>
  `
})
export class AnalysisNlPageComponent {
}
