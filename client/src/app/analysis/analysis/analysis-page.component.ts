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
    <div>
      <div class="options-title">
        Routes without location:
      </div>
      <div class="options">
        <div>
          <a routerLink="/analysis/location/cycling">Cycling</a>
        </div>
        <div>
          <a routerLink="/analysis/location/hiking">Hiking</a>
        </div>
        <div>
          <a routerLink="/analysis/location/horse-riding">Horse riding</a>
        </div>
        <div>
          <a routerLink="/analysis/location/motorboat">Motorboat</a>
        </div>
        <div>
          <a routerLink="/analysis/location/canoe">Canoe</a>
        </div>
        <div>
          <a routerLink="/analysis/location/inline-skating">Inline skating</a>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .buttons {
      display: flex;
    }

    .options-title {
      padding-top: 30px;
      padding-bottom: 10px;
    }
    
    .options {
      margin-left: 20px;
    }
  `]
})
export class AnalysisPageComponent {
}
