import {Component} from "@angular/core";
import {AnalysisModeService} from "./analysis-mode.service";
import {Observable} from "rxjs";

@Component({
  selector: "kpn-analysis-horse-riding-page",
  template: `
    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a>
      <span class="breadcrumb-separator"></span>
      <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      <span class="breadcrumb-separator"></span>
      <ng-container i18n="@@network-type.horse-riding">Horse riding</ng-container>
    </div>

    <kpn-page-header i18n="@@network-type.horse-riding">Horse riding</kpn-page-header>

    <kpn-analysis-mode></kpn-analysis-mode>

    <kpn-icon-button [routerLink]="nlLink | async" icon="netherlands" i18n="@@country.nl">Netherlands</kpn-icon-button>
    <kpn-icon-button [routerLink]="beLink | async" icon="belgium" i18n="@@country.be">Belgium</kpn-icon-button>
  `
})
export class AnalysisHorseRidingPageComponent {

  readonly nlLink: Observable<string>;
  readonly beLink: Observable<string>;

  constructor(private analysisModeService: AnalysisModeService) {
    this.nlLink = analysisModeService.link("horse-riding", "nl");
    this.beLink = analysisModeService.link("horse-riding", "be");
  }

}
