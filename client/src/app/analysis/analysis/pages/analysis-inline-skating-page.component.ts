import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";
import {Observable} from "rxjs";
import {AnalysisModeService} from "./analysis-mode.service";

@Component({
  selector: "kpn-analysis-inline-skating-page",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a></li>
      <li i18n="@@network-type.inline-skating">Inline skating</li>
    </ul>

    <kpn-page-header i18n="@@network-type.inline-skating">Inline skating</kpn-page-header>

    <kpn-analysis-mode></kpn-analysis-mode>

    <kpn-icon-button [routerLink]="nlLink | async" icon="netherlands" i18n="@@country.nl">Netherlands</kpn-icon-button>
  `
})
export class AnalysisInlineSkatingPageComponent {

  readonly nlLink: Observable<string>;

  constructor(private analysisModeService: AnalysisModeService) {
    this.nlLink = analysisModeService.link("inline-skating", "nl");
  }

}
