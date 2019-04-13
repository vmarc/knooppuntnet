import {Component} from "@angular/core";

@Component({
  selector: "kpn-analysis-page",
  template: `

    <div>
      <a routerLink="/">Home</a> >
      Analysis
    </div>

    <h1>
      Analysis
    </h1>

    <div>
      <kpn-icon-button
        routerLink="/analysis/overview"
        icon="overview"
        text="Overview">
      </kpn-icon-button>

      <kpn-icon-button
        routerLink="/analysis/changes"
        icon="changes"
        text="History">
      </kpn-icon-button>
    </div>

    <div>
      <kpn-icon-button
        routerLink="/analysis/nl"
        icon="netherlands"
        text="Netherlands">
      </kpn-icon-button>

      <kpn-icon-button
        routerLink="/analysis/be"
        icon="belgium"
        text="Belgium">
      </kpn-icon-button>

      <kpn-icon-button
        routerLink="/analysis/de"
        icon="germany"
        text="Germany">
      </kpn-icon-button>
    </div>
  `
})
export class AnalysisPageComponent {
}
