import {Component} from "@angular/core";

@Component({
  selector: "kpn-analysis-page",
  template: `

    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a> >
      <span i18n="@@breadcrumb.analysis">Analysis</span>
    </div>

    <kpn-page-header i18n="@@analysis-page.title">Analysis</kpn-page-header>

    <div class="buttons">
      <kpn-icon-button
        routerLink="/analysis/overview"
        icon="overview"
        text="Overview"
        i18n-text="@@analysis-page.overview">
      </kpn-icon-button>

      <kpn-icon-button
        routerLink="/analysis/changes"
        icon="changes"
        text="Changes"
        i18n-text="@@analysis-page.changes">
      </kpn-icon-button>
    </div>

    <div class="buttons">
      <kpn-icon-button
        routerLink="/analysis/nl"
        icon="netherlands"
        text="Netherlands"
        i18n-text="@@country.nl">
      </kpn-icon-button>

      <kpn-icon-button
        routerLink="/analysis/be"
        icon="belgium"
        text="Belgium"
        i18n-text="@@country.be">
      </kpn-icon-button>

      <kpn-icon-button
        routerLink="/analysis/de"
        icon="germany"
        text="Germany"
        i18n-text="@@country.de">
      </kpn-icon-button>

      <kpn-icon-button
        routerLink="/analysis/fr"
        icon="france"
        text="France"
        i18n-text="@@country.fr">
      </kpn-icon-button>
    </div>
  `,
  styles: [`
    .buttons {
      display: flex;
    }
  `]
})
export class AnalysisPageComponent {
}
