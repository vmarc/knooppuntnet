import {Component} from "@angular/core";

@Component({
  selector: "kpn-analysis-hiking-page",
  template: `
    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a> >
      <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a> >
      <ng-container i18n="@@network-type.hiking">Hiking</ng-container>
    </div>

    <kpn-page-header i18n="@@network-type.hiking">Hiking</kpn-page-header>

    <kpn-icon-button
      routerLink="/analysis/hiking/nl"
      icon="netherlands"
      text="Netherlands"
      i18n-text="@@country.nl">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/hiking/be"
      icon="belgium"
      text="Belgium"
      i18n-text="@@country.be">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/hiking/de"
      icon="germany"
      text="Germany"
      i18n-text="@@country.de">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/hiking/fr"
      icon="france"
      text="France"
      i18n-text="@@country.fr">
    </kpn-icon-button>
  `
})
export class AnalysisHikingPageComponent {
}
