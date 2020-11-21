import {ChangeDetectionStrategy} from '@angular/core';
import {Component} from '@angular/core';
import {Observable} from 'rxjs';
import {AnalysisModeService} from './analysis-mode.service';

@Component({
  selector: 'kpn-analysis-horse-riding-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><a routerLink="/analysis" i18n="@@breadcrumb.analysis">Analysis</a></li>
      <li i18n="@@network-type.horse-riding">Horse riding</li>
    </ul>

    <kpn-page-header i18n="@@network-type.horse-riding">Horse riding</kpn-page-header>

    <kpn-analysis-mode></kpn-analysis-mode>

    <kpn-icon-button [routerLink]="nlLink | async" icon="netherlands" i18n="@@country.nl">The Netherlands</kpn-icon-button>
    <kpn-icon-button [routerLink]="beLink | async" icon="belgium" i18n="@@country.be">Belgium</kpn-icon-button>
  `
})
export class AnalysisHorseRidingPageComponent {

  readonly nlLink: Observable<string>;
  readonly beLink: Observable<string>;

  constructor(private analysisModeService: AnalysisModeService) {
    this.nlLink = analysisModeService.link('horse-riding', 'nl');
    this.beLink = analysisModeService.link('horse-riding', 'be');
  }

}
