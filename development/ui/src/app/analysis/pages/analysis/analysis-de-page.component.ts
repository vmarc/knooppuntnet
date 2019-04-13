import {Component} from "@angular/core";

@Component({
  selector: "kpn-analysis-de-page",
  template: `

    <div>
      <a routerLink="/">Home</a> >
      <a routerLink="/analysis">Analysis</a> >
      Germany
    </div>

    <h1>
      Germany
    </h1>

    <kpn-icon-button
      routerLink="/analysis/networks/de/rcn"
      icon="rcn"
      text="Cycling">
    </kpn-icon-button>
  `
})
export class AnalysisDePageComponent {
}
