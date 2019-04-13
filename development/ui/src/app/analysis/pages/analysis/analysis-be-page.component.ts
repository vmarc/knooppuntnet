import {Component} from "@angular/core";

@Component({
  selector: "kpn-analysis-be-page",
  template: `

    <div>
      <a routerLink="/">Home</a> >
      <a routerLink="/analysis">Analysis</a> >
      Belgium
    </div>

    <h1>
      Belgium
    </h1>

    <kpn-icon-button
      routerLink="/analysis/networks/be/rcn"
      icon="rcn"
      text="Cycling">
    </kpn-icon-button>

    <kpn-icon-button
      routerLink="/analysis/networks/be/rwn"
      icon="rwn"
      text="Hiking">
    </kpn-icon-button>
  `
})
export class AnalysisBePageComponent {
}
