import {Component} from "@angular/core";
import {AnalysisModeService} from "./analysis-mode.service";
import {Observable} from "rxjs";

@Component({
  selector: "kpn-analysis-canoe-page",
  template: `

    <div>
      <a routerLink="/" class="breadcrumb-link" i18n="@@breadcrumb.home">Home</a>
      <a routerLink="/analysis" class="breadcrumb-link" i18n="@@breadcrumb.analysis">Analysis</a>
      <ng-container i18n="@@network-type.canoe">Canoe</ng-container>
    </div>

    <kpn-page-header i18n="@@network-type.canoe">Canoe</kpn-page-header>

    <kpn-analysis-mode></kpn-analysis-mode>

    <kpn-icon-button [routerLink]="nlLink | async" icon="netherlands" i18n="@@country.nl">Netherlands</kpn-icon-button>

  `
})
export class AnalysisCanoePageComponent {

  readonly nlLink: Observable<string>;

  constructor(private analysisModeService: AnalysisModeService) {
    this.nlLink = analysisModeService.link("canoe", "nl");
  }

}
