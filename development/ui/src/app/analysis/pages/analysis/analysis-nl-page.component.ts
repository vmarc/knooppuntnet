import {Component} from '@angular/core';

@Component({
  selector: 'kpn-analysis-nl-page',
  template: `

    <div>
      <a routerLink="/">Home</a> >
      <a routerLink="/analysis">Analysis</a> >
      The Netherlands
    </div>

    <h1>
      The Netherlands
    </h1>

    <kpn-icon-button
      routerLink="/analysis/networks/nl/rcn"
      icon="rcn"
      text="Cycling">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/networks/nl/rwn"
      icon="rwn"
      text="Hiking">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/networks/nl/rhn"
      icon="rhn"
      text="Horse">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/networks/nl/rmn"
      icon="rmn"
      text="Motorboat">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/networks/nl/rpn"
      icon="rpn"
      text="Canoe">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/networks/nl/rin"
      icon="rin"
      text="Inline skating">
    </kpn-icon-button>
  `
})
export class AnalysisNlPageComponent {
}
