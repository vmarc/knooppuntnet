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
      routerLink="/analysis/be/cycling/networks"
      icon="cycling"
      text="Cycling"
      i18n-text="@@network-type.cycling">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/be/hiking/networks"
      icon="hiking"
      text="Hiking"
      i18n-text="@@network-type.hiking">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/be/horse/networks"
      icon="horse"
      text="Horse"
      i18n-text="@@network-type.horse">
    </kpn-icon-button>
  `
})
export class AnalysisBePageComponent {
}
