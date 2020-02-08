import {Component} from "@angular/core";
import {AnalysisModeService} from "./analysis-mode.service";
import {Observable} from "rxjs";

@Component({
  selector: "kpn-analysis-cycling-page",
  template: `

    <div>
      <a routerLink="/" i18n="@@breadcrumb.home">Home</a>
      <span class="breadcrumb-separator"></span>
      <a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a>
      <span class="breadcrumb-separator"></span>
      <ng-container i18n="@@network-type.cycling">Cycling</ng-container>
    </div>

    <kpn-page-header i18n="@@network-type.cycling">Cycling</kpn-page-header>

    <kpn-analysis-mode></kpn-analysis-mode>

    <kpn-icon-button [routerLink]="nlLink | async" icon="netherlands" i18n="@@country.nl">Netherlands</kpn-icon-button>
    <kpn-icon-button [routerLink]="beLink | async" icon="belgium" i18n="@@country.be">Belgium</kpn-icon-button>
    <kpn-icon-button [routerLink]="deLink | async" icon="germany" i18n="@@country.de">Germany</kpn-icon-button>
    <kpn-icon-button [routerLink]="frLink | async" icon="france" i18n="@@country.fr">France</kpn-icon-button>
    <kpn-icon-button [routerLink]="atLink | async" icon="austria" i18n="@@country.at">Austria</kpn-icon-button>
  `
})
export class AnalysisCyclingPageComponent {

  readonly nlLink: Observable<string>;
  readonly beLink: Observable<string>;
  readonly deLink: Observable<string>;
  readonly frLink: Observable<string>;
  readonly atLink: Observable<string>;

  constructor(private analysisModeService: AnalysisModeService) {
    this.nlLink = analysisModeService.link("cycling", "nl");
    this.beLink = analysisModeService.link("cycling", "be");
    this.deLink = analysisModeService.link("cycling", "de");
    this.frLink = analysisModeService.link("cycling", "fr");
    this.atLink = analysisModeService.link("cycling", "at");
  }

}
