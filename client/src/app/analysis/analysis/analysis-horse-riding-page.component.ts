import {Component} from "@angular/core";

@Component({
  selector: "kpn-analysis-horse-riding-page",
  template: `
    <div>
      <a routerLink="/" class="breadcrumb-link" i18n="@@breadcrumb.home">Home</a>
      <a routerLink="/analysis" class="breadcrumb-link" i18n="@@breadcrumb.analysis">Analysis</a>
      <ng-container i18n="@@network-type.horse-riding">Horse riding</ng-container>
    </div>

    <kpn-page-header i18n="@@network-type.horse-riding">Horse riding</kpn-page-header>

    <kpn-icon-button routerLink="/analysis/horse-riding/nl" icon="netherlands" i18n="@@country.nl">Netherlands</kpn-icon-button>
    <kpn-icon-button routerLink="/analysis/horse-riding/be" icon="belgium" i18n="@@country.be">Belgium</kpn-icon-button>
  `
})
export class AnalysisHorseRidingPageComponent {
}
