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
        routerLink="/analysis/cycling"
        icon="cycling"
        text="Cycling"
        i18n-text="@@network-type.cycling">
      </kpn-icon-button>

      <kpn-icon-button
        routerLink="/analysis/hiking"
        icon="hiking"
        text="Hiking"
        i18n-text="@@network-type.hiking">
      </kpn-icon-button>

      <kpn-icon-button
        routerLink="/analysis/horse-riding"
        icon="horse-riding"
        text="Horse riding"
        i18n-text="@@network-type.horse-riding">
      </kpn-icon-button>

      <kpn-icon-button
        routerLink="/analysis/motorboat"
        icon="motorboat"
        text="Motorboat"
        i18n-text="@@network-type.motorboat">
      </kpn-icon-button>

      <kpn-icon-button
        routerLink="/analysis/canoe"
        icon="canoe"
        text="Canoe"
        i18n-text="@@network-type.canoe">
      </kpn-icon-button>

      <kpn-icon-button
        routerLink="/analysis/inline-skating"
        icon="inline-skating"
        text="Inline skating"
        i18n-text="@@network-type.inline-skating">
      </kpn-icon-button>
    </div>
  `,
  styles: [`
    .buttons {
      /*display: flex;*/
    }
  `]
})
export class AnalysisPageComponent {
}
