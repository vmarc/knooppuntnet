import {Component} from "@angular/core";

@Component({
  selector: "kpn-analysis-inline-skating-page",
  template: `
    <div>
      <a routerLink="/" class="breadcrumb-link" i18n="@@breadcrumb.home">Home</a>
      <a routerLink="/analysis" class="breadcrumb-link" i18n="@@breadcrumb.analysis">Analysis</a>
      <ng-container i18n="@@network-type.inline-skating">Inline skating</ng-container>
    </div>

    <kpn-page-header i18n="@@network-type.inline-skating">Inline skating</kpn-page-header>

    <kpn-icon-button routerLink="/analysis/inline-skating/nl" icon="netherlands" i18n="@@country.nl">Netherlands</kpn-icon-button>
  `
})
export class AnalysisInlineSkatingPageComponent {
}
